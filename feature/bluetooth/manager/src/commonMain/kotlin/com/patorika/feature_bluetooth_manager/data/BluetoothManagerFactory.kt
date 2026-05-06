package com.patorika.feature_bluetooth_manager.data

import com.patorika.feature_bluetooth_manager.BluetoothManager

expect class BluetoothManagerFactory {
    fun create(): BluetoothManager
}
