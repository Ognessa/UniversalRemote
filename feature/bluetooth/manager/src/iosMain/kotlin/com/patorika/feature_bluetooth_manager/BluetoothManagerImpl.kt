package com.patorika.feature_bluetooth_manager

import com.patorika.feature_bluetooth_manager.BluetoothManager.Companion.BLE_SCAN_DURATION
import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_bluetooth_manager.model.DeviceType
import com.patorika.feature_bluetooth_manager.model.ScanState
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicPropertyWrite
import platform.CoreBluetooth.CBCharacteristicPropertyWriteWithoutResponse
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUUID
import platform.Foundation.dataUsingEncoding
import platform.darwin.NSObject
import kotlin.concurrent.Volatile

// iOS supports BLE only — Classic Bluetooth is not exposed by CoreBluetooth.
// The manager bridges CoreBluetooth's delegate callbacks into StateFlows via two
// helper classes: CentralManagerDelegate (scanning / connection events) and
// PeripheralDelegate (service/characteristic discovery and writes).
internal class BluetoothManagerImpl : BluetoothManager {
    // region State flows

    private val _isBluetoothEnabled = MutableStateFlow(false)
    override val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices

    // Always empty on iOS — CoreBluetooth does not expose Classic discovery.
    private val _discoveredClassicDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val discoveredClassicDevices: StateFlow<List<BluetoothDevice>> = _discoveredClassicDevices

    private val _bleDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val bleDevices: StateFlow<List<BluetoothDevice>> = _bleDevices

    // Classic scan is unavailable on iOS; BLE scan starts in Idle.
    private val _classicScanState = MutableStateFlow<ScanState>(ScanState.Unavailable)
    override val classicScanState: StateFlow<ScanState> = _classicScanState

    private val _bleScanState = MutableStateFlow<ScanState>(ScanState.Idle)
    override val bleScanState: StateFlow<ScanState> = _bleScanState

    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    override val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice

    private val _connectionState = MutableStateFlow<DeviceConnectionState>(DeviceConnectionState.Idle)
    override val connectionState: StateFlow<DeviceConnectionState> = _connectionState

    // endregion

    // region Internal state

    // CoreBluetooth callbacks fire on the main thread, so the scope must use Main to avoid
    // cross-thread StateFlow mutations from a background dispatcher.
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var bleCountdownJob: Job? = null

    // Distinguishes a user-initiated disconnect() from an unexpected radio drop. When true the
    // CentralManagerDelegate skips the automatic reconnect path.
    @Volatile
    private var isIntentionalDisconnect = false

    private var lastConnectedDevice: BluetoothDevice? = null

    // endregion

    // region CoreBluetooth delegates

    private val peripheralDelegate = PeripheralDelegate()
    private val centralDelegate = CentralManagerDelegate()

    // queue=null means CoreBluetooth dispatches its callbacks on the main thread.
    private val centralManager = CBCentralManager(delegate = centralDelegate, queue = null)

    // endregion

    init {
        centralDelegate.onDeviceDiscovered = { device ->
            _bleDevices.update { current ->
                if (current.none { it.id == device.id }) current + device else current
            }
        }

        centralDelegate.onConnected = {
            val peripheral = centralDelegate.currentPeripheral
            if (peripheral != null) {
                peripheral.delegate = peripheralDelegate
                // Service discovery must complete before sendSignal() can resolve a writable
                // characteristic. Result arrives in PeripheralDelegate.
                peripheral.discoverServices(null)
            }
            _connectionState.value = DeviceConnectionState.Connected
        }

        centralDelegate.onDisconnected = { error ->
            if (isIntentionalDisconnect) {
                peripheralDelegate.writableCharacteristic = null
                _connectedDevice.value = null
                _connectionState.value = DeviceConnectionState.Idle
            } else {
                val peripheral = centralDelegate.currentPeripheral
                if (peripheral != null) {
                    // Re-use the same peripheral reference — CoreBluetooth handles the retry.
                    _connectionState.value = DeviceConnectionState.Reconnecting(attempt = 1)
                    centralManager.connectPeripheral(peripheral, options = null)
                } else {
                    _connectedDevice.value = null
                    _connectionState.value =
                        DeviceConnectionState.Error(
                            error?.localizedDescription ?: "Disconnected",
                        )
                }
            }
        }

        centralDelegate.onError = { message ->
            _connectedDevice.value = null
            _connectionState.value = DeviceConnectionState.Error(message)
        }

        centralDelegate.onBluetoothStateChanged = { enabled ->
            _isBluetoothEnabled.value = enabled
        }
    }

    // region Lifecycle

    override fun close() {
        clearDiscoveredDevices()
        centralDelegate.currentPeripheral?.let { centralManager.cancelPeripheralConnection(it) }
    }

    // endregion

    // region Classic stubs — not supported on iOS

    override fun loadPairedDevices() = Unit

    override fun startClassicScan() = Unit

    override fun stopClassicScan() = Unit

    // endregion

    // region BLE scanning

    override fun startBleScan() {
        bleCountdownJob?.cancel()
        _bleDevices.value = emptyList()
        if (centralManager.state != CBCentralManagerStatePoweredOn) {
            _bleScanState.value = ScanState.Error("Bluetooth not available")
            return
        }
        _bleScanState.value = ScanState.Scanning(BLE_SCAN_DURATION)
        // serviceUUIDs=null scans for all nearby peripherals.
        centralManager.scanForPeripheralsWithServices(serviceUUIDs = null, options = null)
        bleCountdownJob =
            scope.launch {
                for (remaining in (BLE_SCAN_DURATION - 1) downTo 0) {
                    delay(1_000)
                    if (_bleScanState.value is ScanState.Scanning) {
                        _bleScanState.value = ScanState.Scanning(remaining)
                    } else {
                        return@launch
                    }
                }
                if (_bleScanState.value is ScanState.Scanning) {
                    centralManager.stopScan()
                    _bleScanState.value = ScanState.Finished
                }
            }
    }

    override fun stopBleScan() {
        centralManager.stopScan()
        bleCountdownJob?.cancel()
        if (_bleScanState.value is ScanState.Scanning) {
            _bleScanState.value = ScanState.Finished
        }
    }

    // endregion

    // region Connection

    override fun connectToDevice(device: BluetoothDevice) {
        isIntentionalDisconnect = false
        lastConnectedDevice = device
        _connectionState.value = DeviceConnectionState.Connecting
        _connectedDevice.value = device
        // On iOS, peripherals are identified by a system-assigned UUID (not a MAC address).
        val uuid =
            NSUUID(uUIDString = device.id) ?: run {
                _connectedDevice.value = null
                _connectionState.value = DeviceConnectionState.Error("Invalid device UUID")
                return
            }
        val peripheral =
            centralManager
                .retrievePeripheralsWithIdentifiers(listOf(uuid))
                .firstOrNull() as? CBPeripheral ?: run {
                _connectedDevice.value = null
                _connectionState.value = DeviceConnectionState.Error("Device not found")
                return
            }
        centralDelegate.currentPeripheral = peripheral
        centralManager.connectPeripheral(peripheral, options = null)
    }

    override fun disconnect() {
        isIntentionalDisconnect = true
        lastConnectedDevice = null
        _connectionState.value = DeviceConnectionState.Disconnecting
        peripheralDelegate.writableCharacteristic = null
        centralDelegate.currentPeripheral?.let { centralManager.cancelPeripheralConnection(it) }
        centralDelegate.currentPeripheral = null
        _connectedDevice.value = null
        _connectionState.value = DeviceConnectionState.Idle
    }

    override fun clearDiscoveredDevices() {
        _pairedDevices.value = emptyList()
        _discoveredClassicDevices.value = emptyList()
        _bleDevices.value = emptyList()
        stopClassicScan()
        stopBleScan()
    }

    // endregion

    // region Signal transmission

    override fun sendSignal(signal: String) {
        val peripheral = centralDelegate.currentPeripheral ?: return
        val characteristic = peripheralDelegate.writableCharacteristic ?: return
        val data = (signal as? NSString)?.dataUsingEncoding(NSUTF8StringEncoding) ?: return
        val writeType =
            if ((characteristic.properties and CBCharacteristicPropertyWriteWithoutResponse) != 0UL) {
                CBCharacteristicWriteWithoutResponse
            } else {
                CBCharacteristicWriteWithResponse
            }
        peripheral.writeValue(data, forCharacteristic = characteristic, type = writeType)
    }

    // endregion
}

// Handles characteristic discovery and write operations for a connected peripheral.
private class PeripheralDelegate :
    NSObject(),
    CBPeripheralDelegateProtocol {
    var writableCharacteristic: CBCharacteristic? = null

    override fun peripheral(
        peripheral: CBPeripheral,
        didDiscoverServices: NSError?,
    ) {
        peripheral.services?.forEach { service ->
            peripheral.discoverCharacteristics(null, forService = service as CBService)
        }
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didDiscoverCharacteristicsForService: CBService,
        error: NSError?,
    ) {
        if (writableCharacteristic != null) return
        // Pick the first writable characteristic — used by sendSignal().
        writableCharacteristic =
            didDiscoverCharacteristicsForService.characteristics
                ?.filterIsInstance<CBCharacteristic>()
                ?.firstOrNull {
                    (it.properties and CBCharacteristicPropertyWrite) != 0UL ||
                        (it.properties and CBCharacteristicPropertyWriteWithoutResponse) != 0UL
                }
    }
}

// Bridges CBCentralManager delegate callbacks into lambdas consumed by BluetoothManagerImpl.
private class CentralManagerDelegate :
    NSObject(),
    CBCentralManagerDelegateProtocol {
    var currentPeripheral: CBPeripheral? = null
    var onDeviceDiscovered: ((BluetoothDevice) -> Unit)? = null
    var onConnected: (() -> Unit)? = null
    var onDisconnected: ((NSError?) -> Unit)? = null
    var onError: ((String) -> Unit)? = null
    var onBluetoothStateChanged: ((Boolean) -> Unit)? = null

    override fun centralManager(
        central: CBCentralManager,
        didDiscoverPeripheral: CBPeripheral,
        advertisementData: Map<Any?, *>,
        RSSI: NSNumber,
    ) {
        val id = didDiscoverPeripheral.identifier.UUIDString
        val name = didDiscoverPeripheral.name
        onDeviceDiscovered?.invoke(BluetoothDevice(id = id, name = name, type = DeviceType.BLE))
    }

    override fun centralManager(
        central: CBCentralManager,
        didConnectPeripheral: CBPeripheral,
    ) {
        onConnected?.invoke()
    }

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        onBluetoothStateChanged?.invoke(central.state == CBCentralManagerStatePoweredOn)
    }

    @ObjCSignatureOverride
    override fun centralManager(
        central: CBCentralManager,
        didDisconnectPeripheral: CBPeripheral,
        error: NSError?,
    ) {
        currentPeripheral = null
        onDisconnected?.invoke(error)
    }

    @ObjCSignatureOverride
    override fun centralManager(
        central: CBCentralManager,
        didFailToConnectPeripheral: CBPeripheral,
        error: NSError?,
    ) {
        currentPeripheral = null
        onError?.invoke(error?.localizedDescription ?: "Connection failed")
    }
}
