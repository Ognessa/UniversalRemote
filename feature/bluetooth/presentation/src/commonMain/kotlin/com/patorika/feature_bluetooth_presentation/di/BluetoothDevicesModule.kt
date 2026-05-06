package com.patorika.feature_bluetooth_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_bluetooth_api.BluetoothDevicesScreenBuilder
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_presentation.api.BluetoothDevicesScreenBuilderImpl
import com.patorika.feature_bluetooth_presentation.ui.DevicePickerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothDevicesModule =
    module {
        viewModel {
            DevicePickerViewModel(bluetoothManager = get<BluetoothManager>())
        }

        factory<BluetoothDevicesScreenBuilder> {
            BluetoothDevicesScreenBuilderImpl()
        } bind ScreenBuilder::class
    }
