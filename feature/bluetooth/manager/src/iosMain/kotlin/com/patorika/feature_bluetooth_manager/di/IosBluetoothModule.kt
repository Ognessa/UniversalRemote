package com.patorika.feature_bluetooth_manager.di

import com.patorika.feature_bluetooth_manager.data.BluetoothManagerFactory
import org.koin.dsl.module

val iosBluetoothModule =
    module {
        factory { BluetoothManagerFactory() }
    }
