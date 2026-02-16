package com.patorika.universalremote.feature.editor.main.di

import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.editor.api.state.EditorSharedState
import com.patorika.universalremote.feature.editor.main.ui.ControllerEditorViewModel
import com.patorika.universalremote.feature.library.di.EditorLibraryScreenBuilder
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val controllerEditorModule =
    module {
        viewModel {
            ControllerEditorViewModel(
                editorSharedState = get<EditorSharedState>(),
            )
        }

        factory {
            ControllerEditorScreenBuilder(
                editorLibraryScreenBuilder = get<EditorLibraryScreenBuilder>(),
            )
        } bind ScreenBuilder::class
    }
