package com.patorika.feature_bluetooth_manager.di

import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_manager.data.BluetoothManagerFactory
import org.koin.dsl.module
import org.koin.dsl.onClose

val bluetoothManagerModule =
    module {
        single<BluetoothManager> {
            get<BluetoothManagerFactory>().create()
        } onClose { it?.close() }
    }
