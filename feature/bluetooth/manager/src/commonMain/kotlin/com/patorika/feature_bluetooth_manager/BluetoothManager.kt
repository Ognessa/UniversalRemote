package com.patorika.feature_bluetooth_manager

import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_bluetooth_manager.model.ScanState
import kotlinx.coroutines.flow.StateFlow

interface BluetoothManager {
    // --- Observable state ---

    val isBluetoothEnabled: StateFlow<Boolean>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val discoveredClassicDevices: StateFlow<List<BluetoothDevice>>
    val bleDevices: StateFlow<List<BluetoothDevice>>
    val classicScanState: StateFlow<ScanState>
    val bleScanState: StateFlow<ScanState>
    val connectedDevice: StateFlow<BluetoothDevice?>
    val connectionState: StateFlow<DeviceConnectionState>

    // --- Actions ---

    fun loadPairedDevices()

    fun startClassicScan()

    fun stopClassicScan()

    fun startBleScan()

    fun stopBleScan()

    fun connectToDevice(device: BluetoothDevice)

    fun disconnect()

    fun sendSignal(signal: String)

    fun clearDiscoveredDevices()

    // Must be called when the manager is no longer needed to release platform resources
    // (e.g. unregister the Android BroadcastReceiver).
    fun close()

    companion object {
        const val CLASSIC_SCAN_DURATION = 12
        const val BLE_SCAN_DURATION = 10
    }
}
