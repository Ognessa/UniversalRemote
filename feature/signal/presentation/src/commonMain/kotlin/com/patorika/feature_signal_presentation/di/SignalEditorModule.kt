package com.patorika.feature_signal_presentation.di

import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_signal_api.SignalEditorScreenBuilder
import com.patorika.feature_signal_presentation.api.SignalEditorScreenBuilderImpl
import com.patorika.feature_signal_presentation.ui.SignalEditorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val signalEditorModule =
    module {
        viewModel { (controllerElement: ControllerElementModel) ->
            SignalEditorViewModel(
                elementData = controllerElement,
                editorSharedState = get<EditorSharedState>(),
            )
        }

        factory<SignalEditorScreenBuilder> {
            SignalEditorScreenBuilderImpl()
        } bind ScreenBuilder::class
    }
