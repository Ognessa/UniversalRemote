package com.patorika.feature_bluetooth_presentation.model

import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_bluetooth_manager.model.ScanState

data class DevicePickerUiState(
    val isBluetoothEnabled: Boolean = false,
    val devices: DeviceListState = DeviceListState(),
    val scanState: ScanStateModel = ScanStateModel(),
    val connectionState: ConnectionState = ConnectionState(),
) {
    fun checkDeviceConnectionState(device: BluetoothDevice): DeviceConnectionState =
        if (device.id == connectionState.device?.id) {
            connectionState.state
        } else {
            DeviceConnectionState.Idle
        }
}

data class DeviceListState(
    val paired: List<BluetoothDevice> = emptyList(),
    val discoveredClassic: List<BluetoothDevice> = emptyList(),
    val ble: List<BluetoothDevice> = emptyList(),
)

data class ScanStateModel(
    val classic: ScanState = ScanState.Idle,
    val ble: ScanState = ScanState.Idle,
)

data class ConnectionState(
    val device: BluetoothDevice? = null,
    val state: DeviceConnectionState = DeviceConnectionState.Idle,
)
