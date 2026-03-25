package com.patorika.feature_editor_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_editor_presentation.api.ControllerEditorScreenBuilderImpl
import com.patorika.feature_editor_presentation.api.EditorSharedStateImpl
import com.patorika.feature_editor_presentation.model.ControllerEditorParams
import com.patorika.feature_editor_presentation.ui.ControllerEditorViewModel
import com.patorika.feature_editor_presentation.usecase.GetControllerByIdUseCase
import com.patorika.feature_editor_presentation.usecase.SaveControllerUseCase
import com.patorika.feature_library_api.EditorLibraryScreenBuilder
import com.patorika.feature_signal_api.SignalEditorScreenBuilder
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val controllerEditorModule =
    module {
        single<EditorSharedState> { EditorSharedStateImpl() }

        viewModel {
            ControllerEditorViewModel(
                params = get<ControllerEditorParams>(),
                appNotificationManager = get<AppNotificationManager>(),
                editorSharedState = get<EditorSharedState>(),
                saveControllerUseCase = get<SaveControllerUseCase>(),
                getControllerByIdUseCase = get<GetControllerByIdUseCase>(),
            )
        }

        factory<ControllerEditorScreenBuilder> {
            ControllerEditorScreenBuilderImpl(
                editorLibraryScreenBuilder = get<EditorLibraryScreenBuilder>(),
                signalEditorScreenBuilder = get<SignalEditorScreenBuilder>(),
            )
        } bind ScreenBuilder::class

        factory {
            SaveControllerUseCase(
                repository = get<ControllerRepository>(),
            )
        }

        factory {
            GetControllerByIdUseCase(
                repository = get<ControllerRepository>(),
            )
        }
    }
