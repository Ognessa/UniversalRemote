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

internal class BluetoothManagerImpl : BluetoothManager {
    private val _isBluetoothEnabled = MutableStateFlow(false)
    override val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices

    private val _discoveredClassicDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val discoveredClassicDevices: StateFlow<List<BluetoothDevice>> =
        _discoveredClassicDevices

    private val _bleDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val bleDevices: StateFlow<List<BluetoothDevice>> = _bleDevices

    private val _classicScanState = MutableStateFlow<ScanState>(ScanState.Unavailable)
    override val classicScanState: StateFlow<ScanState> = _classicScanState

    private val _bleScanState = MutableStateFlow<ScanState>(ScanState.Idle)
    override val bleScanState: StateFlow<ScanState> = _bleScanState

    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    override val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice

    private val _connectionState =
        MutableStateFlow<DeviceConnectionState>(DeviceConnectionState.Idle)
    override val connectionState: StateFlow<DeviceConnectionState> = _connectionState

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var bleCountdownJob: Job? = null
    private var isIntentionalDisconnect = false
    private var lastConnectedDevice: BluetoothDevice? = null

    private val peripheralDelegate = PeripheralDelegate()
    private val centralDelegate = CentralManagerDelegate()
    private val centralManager = CBCentralManager(delegate = centralDelegate, queue = null)

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

    override fun loadPairedDevices() = Unit

    override fun startClassicScan() = Unit

    override fun stopClassicScan() = Unit

    override fun startBleScan() {
        bleCountdownJob?.cancel()
        _bleDevices.value = emptyList()
        if (centralManager.state != CBCentralManagerStatePoweredOn) {
            _bleScanState.value = ScanState.Error("Bluetooth not available")
            return
        }
        _bleScanState.value = ScanState.Scanning(BLE_SCAN_DURATION)
        centralManager.scanForPeripheralsWithServices(serviceUUIDs = null, options = null)
        bleCountdownJob =
            scope.launch {
                for (remaining in (BLE_SCAN_DURATION - 1) downTo 0) {
                    delay(1_000)
                    if (_bleScanState.value is ScanState.Scanning) {
                        _bleScanState.value = ScanState.Scanning(remaining)
                    } else {
                        break
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

    override fun connectToDevice(device: BluetoothDevice) {
        isIntentionalDisconnect = false
        lastConnectedDevice = device
        _connectionState.value = DeviceConnectionState.Connecting
        _connectedDevice.value = device
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
}

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
        writableCharacteristic =
            didDiscoverCharacteristicsForService.characteristics
                ?.filterIsInstance<CBCharacteristic>()
                ?.firstOrNull {
                    (it.properties and CBCharacteristicPropertyWrite) != 0UL ||
                        (it.properties and CBCharacteristicPropertyWriteWithoutResponse) != 0UL
                }
    }
}

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
