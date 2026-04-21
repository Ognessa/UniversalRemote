package com.patorika.feature_bluetooth_presentation.model

sealed interface BluetoothDevicesEvents {
    data object Close : BluetoothDevicesEvents

    data object Refresh : BluetoothDevicesEvents

    data class DeviceClicked(
        val address: String,
    ) : BluetoothDevicesEvents
}
