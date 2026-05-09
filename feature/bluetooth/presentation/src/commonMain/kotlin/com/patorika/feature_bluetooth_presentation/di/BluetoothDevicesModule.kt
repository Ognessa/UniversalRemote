package com.patorika.feature_bluetooth_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_bluetooth_api.DevicePickerScreenBuilder
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_presentation.api.DevicePickerScreenBuilderImpl
import com.patorika.feature_bluetooth_presentation.ui.DevicePickerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothDevicesModule =
    module {
        viewModel {
            DevicePickerViewModel(bluetoothManager = get<BluetoothManager>())
        }

        factory<DevicePickerScreenBuilder> {
            DevicePickerScreenBuilderImpl()
        } bind ScreenBuilder::class
    }
