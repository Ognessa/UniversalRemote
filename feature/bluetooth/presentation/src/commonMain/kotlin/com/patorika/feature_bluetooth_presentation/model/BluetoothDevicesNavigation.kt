package com.patorika.feature_bluetooth_presentation.model

sealed interface BluetoothDevicesNavigation {
    data object Close : BluetoothDevicesNavigation

    data class DeviceSelected(
        val address: String,
    ) : BluetoothDevicesNavigation
}
