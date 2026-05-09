package com.patorika.feature_playground_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_bluetooth_api.DevicePickerScreenBuilder
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_playground_api.api.PlaygroundScreenBuilder
import com.patorika.feature_playground_presentation.api.PlaygroundScreenBuilderImpl
import com.patorika.feature_playground_presentation.ui.PlaygroundViewModel
import com.patorika.feature_playground_presentation.useCase.GetControllerByIdUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val playgroundModule =
    module {
        viewModel {
            PlaygroundViewModel(
                controllerId = get<String>(),
                getControllerByIdUseCase = get<GetControllerByIdUseCase>(),
                bluetoothManager = get<BluetoothManager>(),
            )
        }

        factory<PlaygroundScreenBuilder> {
            PlaygroundScreenBuilderImpl(
                bluetoothDevicesScreenBuilder = get<DevicePickerScreenBuilder>(),
            )
        } bind ScreenBuilder::class

        factory {
            GetControllerByIdUseCase(
                repository = get<ControllerRepository>(),
            )
        }
    }
