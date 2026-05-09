package com.patorika.feature_bluetooth_manager.data

import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_manager.BluetoothManagerImpl

actual class BluetoothManagerFactory {
    actual fun create(): BluetoothManager = BluetoothManagerImpl()
}
