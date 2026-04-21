package com.patorika.feature_bluetooth_presentation.model

data class BluetoothDevicesScreenState(
    val isLoading: Boolean = false,
    val devices: List<BluetoothDeviceModel> = emptyList(),
)
