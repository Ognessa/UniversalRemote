package com.patorika.feature_bluetooth_manager.di

import com.patorika.feature_bluetooth_manager.data.BluetoothManagerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidBluetoothModule =
    module {
        factory { BluetoothManagerFactory(context = androidContext()) }
    }
