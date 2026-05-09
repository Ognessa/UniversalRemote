package com.patorika.feature_bluetooth_manager.model

data class BluetoothDevice(
    val id: String,
    val name: String?,
    val type: DeviceType,
)
