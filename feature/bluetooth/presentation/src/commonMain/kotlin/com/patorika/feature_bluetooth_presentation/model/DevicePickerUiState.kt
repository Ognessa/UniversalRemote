package com.patorika.feature_bluetooth_presentation.model

import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_bluetooth_manager.model.ScanState

data class DevicePickerUiState(
    val isBluetoothEnabled: Boolean = false,
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val discoveredClassicDevices: List<BluetoothDevice> = emptyList(),
    val bleDevices: List<BluetoothDevice> = emptyList(),
    val classicScanState: ScanState = ScanState.Idle,
    val bleScanState: ScanState = ScanState.Idle,
    val connectedDevice: BluetoothDevice? = null,
    val connectionState: DeviceConnectionState = DeviceConnectionState.Idle,
) {
    fun checkDeviceConnectionState(device: BluetoothDevice): DeviceConnectionState =
        if (device.id == connectedDevice?.id) {
            connectionState
        } else {
            DeviceConnectionState.Idle
        }
}
