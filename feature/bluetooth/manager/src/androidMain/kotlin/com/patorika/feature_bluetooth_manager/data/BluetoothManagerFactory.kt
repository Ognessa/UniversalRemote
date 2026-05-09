package com.patorika.feature_bluetooth_manager.data

import android.content.Context
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_manager.BluetoothManagerImpl

actual class BluetoothManagerFactory(private val context: Context) {
    actual fun create(): BluetoothManager = BluetoothManagerImpl(context)
}
