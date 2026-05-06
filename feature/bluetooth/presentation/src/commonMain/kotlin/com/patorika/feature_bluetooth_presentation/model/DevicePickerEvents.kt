package com.patorika.feature_bluetooth_presentation.model

import com.patorika.feature_bluetooth_manager.model.BluetoothDevice

sealed interface DevicePickerEvents {
    data object Close : DevicePickerEvents

    data object StartClassicScan : DevicePickerEvents

    data object StartBleScan : DevicePickerEvents

    data class ConnectToDevice(
        val device: BluetoothDevice,
    ) : DevicePickerEvents

    data object Disconnect : DevicePickerEvents
}
