package com.patorika.feature_bluetooth_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_bluetooth_api.BluetoothDevicesScreenBuilder
import com.patorika.feature_bluetooth_presentation.api.BluetoothDevicesScreenBuilderImpl
import com.patorika.feature_bluetooth_presentation.ui.BluetoothDevicesViewModel
import com.patorika.feature_bluetooth_presentation.usecase.GetBluetoothDevicesUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothDevicesModule =
    module {
        viewModel {
            BluetoothDevicesViewModel(
                getBluetoothDevicesUseCase = get<GetBluetoothDevicesUseCase>(),
            )
        }

        factory<BluetoothDevicesScreenBuilder> {
            BluetoothDevicesScreenBuilderImpl()
        } bind ScreenBuilder::class

        factory {
            GetBluetoothDevicesUseCase()
        }
    }
