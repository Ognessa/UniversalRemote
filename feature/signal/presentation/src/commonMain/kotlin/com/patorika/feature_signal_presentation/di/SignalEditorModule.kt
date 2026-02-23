package com.patorika.feature_signal_presentation.di

import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_signal_api.SignalEditorScreenBuilder
import com.patorika.feature_signal_presentation.api.SignalEditorScreenBuilderImpl
import com.patorika.feature_signal_presentation.ui.SignalEditorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val signalEditorModule =
    module {
        viewModel { (controllerElement: ControllerModel) ->
            SignalEditorViewModel(
                element = controllerElement,
            )
        }

        factory<SignalEditorScreenBuilder> {
            SignalEditorScreenBuilderImpl()
        } bind ScreenBuilder::class
    }
