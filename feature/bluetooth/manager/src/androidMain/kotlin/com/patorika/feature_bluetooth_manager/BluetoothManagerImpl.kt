package com.patorika.feature_bluetooth_manager

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import com.patorika.feature_bluetooth_manager.BluetoothManager.Companion.BLE_SCAN_DURATION
import com.patorika.feature_bluetooth_manager.BluetoothManager.Companion.CLASSIC_SCAN_DURATION
import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_bluetooth_manager.model.DeviceType
import com.patorika.feature_bluetooth_manager.model.ScanState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

internal class BluetoothManagerImpl(
    private val context: Context,
) : BluetoothManager {
    private val btManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
    private val adapter: BluetoothAdapter? = btManager.adapter
    private val leScanner get() = adapter?.bluetoothLeScanner

    private val _isBluetoothEnabled = MutableStateFlow(adapter?.isEnabled ?: false)
    override val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices

    private val _discoveredClassicDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val discoveredClassicDevices: StateFlow<List<BluetoothDevice>> =
        _discoveredClassicDevices

    private val _bleDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val bleDevices: StateFlow<List<BluetoothDevice>> = _bleDevices

    private val _classicScanState = MutableStateFlow<ScanState>(ScanState.Idle)
    override val classicScanState: StateFlow<ScanState> = _classicScanState

    private val _bleScanState = MutableStateFlow<ScanState>(ScanState.Idle)
    override val bleScanState: StateFlow<ScanState> = _bleScanState

    private val _connectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    override val connectedDevice: StateFlow<BluetoothDevice?> = _connectedDevice

    private val _connectionState =
        MutableStateFlow<DeviceConnectionState>(DeviceConnectionState.Idle)
    override val connectionState: StateFlow<DeviceConnectionState> = _connectionState

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var classicCountdownJob: Job? = null
    private var bleCountdownJob: Job? = null
    private var classicSocket: BluetoothSocket? = null
    private var currentGatt: BluetoothGatt? = null
    private var writableCharacteristic: BluetoothGattCharacteristic? = null
    private var currentConnectionType: DeviceType? = null
    private var isIntentionalDisconnect = false
    private var lastConnectedDevice: BluetoothDevice? = null
    private var reconnectJob: Job? = null

    private val discoveryReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent,
            ) {
                when (intent.action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state =
                            intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        _isBluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
                    }

                    android.bluetooth.BluetoothDevice.ACTION_FOUND -> {
                        val btDevice: android.bluetooth.BluetoothDevice? =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                intent.getParcelableExtra(
                                    android.bluetooth.BluetoothDevice.EXTRA_DEVICE,
                                    android.bluetooth.BluetoothDevice::class.java,
                                )
                            } else {
                                intent.getParcelableExtra(android.bluetooth.BluetoothDevice.EXTRA_DEVICE)
                            }
                        btDevice?.let { device ->
                            val model =
                                BluetoothDevice(
                                    id = device.address,
                                    name = if (hasConnectPermission()) device.name else null,
                                    type = DeviceType.CLASSIC,
                                )
                            _discoveredClassicDevices.update { current ->
                                if (
                                    current.none { it.id == model.id } &&
                                    _pairedDevices.value.none { it.id == model.id }
                                ) {
                                    current + model
                                } else {
                                    current
                                }
                            }
                        }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        classicCountdownJob?.cancel()
                        _classicScanState.value = ScanState.Finished
                    }

                    android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        if (isIntentionalDisconnect) return
                        val device = lastConnectedDevice ?: return
                        if (device.type != DeviceType.CLASSIC) return
                        retryClassicConnection(device)
                    }
                }
            }
        }

    private val bleScanCallback =
        object : ScanCallback() {
            override fun onScanResult(
                callbackType: Int,
                result: ScanResult,
            ) {
                val btDevice = result.device
                val model =
                    BluetoothDevice(
                        id = btDevice.address,
                        name = if (hasConnectPermission()) btDevice.name else null,
                        type = DeviceType.BLE,
                    )
                _bleDevices.update { current ->
                    if (current.none { it.id == model.id }) current + model else current
                }
            }

            override fun onScanFailed(errorCode: Int) {
                bleCountdownJob?.cancel()
                _bleScanState.value = ScanState.Error("BLE scan failed: code $errorCode")
            }
        }

    private val gattCallback: BluetoothGattCallback =
        object : BluetoothGattCallback() {
            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int,
            ) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        _connectionState.value = DeviceConnectionState.Connected
                        if (hasConnectPermission()) gatt.discoverServices()
                    }

                    BluetoothProfile.STATE_DISCONNECTED -> {
                        if (isIntentionalDisconnect) {
                            writableCharacteristic = null
                            _connectedDevice.value = null
                            _connectionState.value = DeviceConnectionState.Idle
                            currentGatt?.close()
                            currentGatt = null
                            currentConnectionType = null
                        } else {
                            val device = lastConnectedDevice
                            if (device != null) {
                                writableCharacteristic = null
                                currentGatt?.close()
                                currentGatt = null
                                _connectionState.value =
                                    DeviceConnectionState.Reconnecting(attempt = 1)
                                val btDevice = adapter?.getRemoteDevice(device.id) ?: return
                                if (hasConnectPermission()) {
                                    currentGatt = btDevice.connectGatt(context, true, gattCallback)
                                }
                            } else {
                                writableCharacteristic = null
                                _connectedDevice.value = null
                                _connectionState.value = DeviceConnectionState.Idle
                                currentGatt?.close()
                                currentGatt = null
                                currentConnectionType = null
                            }
                        }
                    }
                }
            }

            override fun onServicesDiscovered(
                gatt: BluetoothGatt,
                status: Int,
            ) {
                if (status != BluetoothGatt.GATT_SUCCESS) return
                writableCharacteristic =
                    gatt.services
                        .flatMap { it.characteristics }
                        .firstOrNull { char ->
                            (char.properties and BluetoothGattCharacteristic.PROPERTY_WRITE) != 0 ||
                                (char.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0
                        }
            }
        }

    init {
        val filter =
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
                addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        context.applicationContext.registerReceiver(discoveryReceiver, filter)
        loadPairedDevices()
    }

    override fun loadPairedDevices() {
        if (!hasConnectPermission()) return
        _pairedDevices.value =
            adapter?.bondedDevices?.map { device ->
                val type =
                    when (device.type) {
                        android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE -> DeviceType.BLE
                        else -> DeviceType.CLASSIC
                    }
                BluetoothDevice(id = device.address, name = device.name, type = type)
            } ?: emptyList()
    }

    override fun startClassicScan() {
        if (!hasScanPermission() || !hasLocationPermission()) return
        _discoveredClassicDevices.value = emptyList()
        _classicScanState.value = ScanState.Scanning(CLASSIC_SCAN_DURATION)
        adapter?.startDiscovery()
        classicCountdownJob?.cancel()
        classicCountdownJob =
            scope.launch {
                for (remaining in (CLASSIC_SCAN_DURATION - 1) downTo 0) {
                    delay(1_000)
                    if (_classicScanState.value is ScanState.Scanning) {
                        _classicScanState.value = ScanState.Scanning(remaining)
                    } else {
                        break
                    }
                }
                if (_classicScanState.value is ScanState.Scanning) {
                    _classicScanState.value = ScanState.Finished
                }
            }
    }

    override fun stopClassicScan() {
        if (hasScanPermission()) adapter?.cancelDiscovery()
        classicCountdownJob?.cancel()
        if (_classicScanState.value is ScanState.Scanning) {
            _classicScanState.value = ScanState.Finished
        }
    }

    override fun startBleScan() {
        if (!hasScanPermission()) {
            _bleScanState.value = ScanState.Error("Bluetooth scan permission not granted")
            return
        }
        val scanner =
            leScanner ?: run {
                _bleScanState.value = ScanState.Error("Bluetooth not available")
                return
            }
        _bleDevices.value = emptyList()
        _bleScanState.value = ScanState.Scanning(BLE_SCAN_DURATION)
        scanner.startScan(bleScanCallback)
        bleCountdownJob?.cancel()
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
                    if (hasScanPermission()) scanner.stopScan(bleScanCallback)
                    _bleScanState.value = ScanState.Finished
                }
            }
    }

    override fun stopBleScan() {
        if (hasScanPermission()) leScanner?.stopScan(bleScanCallback)
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
        when (device.type) {
            DeviceType.CLASSIC -> connectClassic(device)
            DeviceType.BLE -> connectBle(device)
        }
    }

    private fun connectClassic(device: BluetoothDevice) {
        scope.launch {
            try {
                if (hasScanPermission()) adapter?.cancelDiscovery()
                val btDevice =
                    adapter?.getRemoteDevice(device.id) ?: run {
                        _connectedDevice.value = null
                        _connectionState.value = DeviceConnectionState.Error("Device not found")
                        return@launch
                    }
                val socket = btDevice.createRfcommSocketToServiceRecord(SPP_UUID)
                socket.connect()
                classicSocket = socket
                currentConnectionType = DeviceType.CLASSIC
                _connectionState.value = DeviceConnectionState.Connected
            } catch (e: IOException) {
                _connectedDevice.value = null
                _connectionState.value =
                    DeviceConnectionState.Error(e.message ?: "Connection failed")
            }
        }
    }

    private fun connectBle(device: BluetoothDevice) {
        if (!hasConnectPermission()) {
            _connectedDevice.value = null
            _connectionState.value =
                DeviceConnectionState.Error("Bluetooth connect permission not granted")
            return
        }
        val btDevice =
            adapter?.getRemoteDevice(device.id) ?: run {
                _connectedDevice.value = null
                _connectionState.value = DeviceConnectionState.Error("Device not found")
                return
            }
        currentGatt?.close()
        currentConnectionType = DeviceType.BLE
        currentGatt = btDevice.connectGatt(context, false, gattCallback)
    }

    override fun disconnect() {
        isIntentionalDisconnect = true
        lastConnectedDevice = null
        reconnectJob?.cancel()
        reconnectJob = null
        _connectionState.value = DeviceConnectionState.Disconnecting
        try {
            classicSocket?.close()
        } catch (_: IOException) {
        }
        classicSocket = null
        if (hasConnectPermission()) currentGatt?.disconnect()
        currentGatt?.close()
        currentGatt = null
        writableCharacteristic = null
        currentConnectionType = null
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
        when (currentConnectionType) {
            DeviceType.CLASSIC -> {
                val bytes = "$signal\n".toByteArray(Charsets.UTF_8)

                scope.launch {
                    try {
                        classicSocket?.outputStream?.write(bytes)
                    } catch (_: IOException) {
                    }
                }
            }

            DeviceType.BLE -> {
                val bytes = signal.toByteArray(Charsets.UTF_8)

                val gatt = currentGatt ?: return
                val characteristic = writableCharacteristic ?: return
                if (!hasConnectPermission()) return
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val writeType =
                        if ((characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0) {
                            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                        } else {
                            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                        }
                    gatt.writeCharacteristic(characteristic, bytes, writeType)
                } else {
                    characteristic.value = bytes
                    characteristic.writeType =
                        if ((characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0) {
                            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                        } else {
                            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                        }
                    gatt.writeCharacteristic(characteristic)
                }
            }

            null -> {}
        }
    }

    private fun retryClassicConnection(device: BluetoothDevice) {
        reconnectJob?.cancel()
        reconnectJob =
            scope.launch {
                for (attempt in 1..CLASSIC_RECONNECT_MAX_ATTEMPTS) {
                    if (isIntentionalDisconnect) return@launch
                    _connectionState.value = DeviceConnectionState.Reconnecting(attempt = attempt)
                    try {
                        if (hasScanPermission()) adapter?.cancelDiscovery()
                        val btDevice = adapter?.getRemoteDevice(device.id) ?: break
                        val socket = btDevice.createRfcommSocketToServiceRecord(SPP_UUID)
                        socket.connect()
                        classicSocket = socket
                        currentConnectionType = DeviceType.CLASSIC
                        _connectionState.value = DeviceConnectionState.Connected
                        return@launch
                    } catch (_: IOException) {
                    }
                    delay(CLASSIC_RECONNECT_DELAY_MS)
                }
                _connectionState.value = DeviceConnectionState.Error("Device lost")
                _connectedDevice.value = null
                lastConnectedDevice = null
            }
    }

    private fun hasScanPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        }

    private fun hasConnectPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            context.checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        }

    private fun hasLocationPermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val CLASSIC_RECONNECT_MAX_ATTEMPTS = 5
        private const val CLASSIC_RECONNECT_DELAY_MS = 3_000L
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}
