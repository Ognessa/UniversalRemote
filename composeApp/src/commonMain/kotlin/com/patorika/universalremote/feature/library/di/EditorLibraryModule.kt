package com.patorika.universalremote.feature.library.di

import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.editor.api.state.EditorSharedState
import com.patorika.universalremote.feature.library.ui.EditorLibraryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val editorLibraryModule =
    module {
        viewModel {
            EditorLibraryViewModel(
                editorSharedState = get<EditorSharedState>(),
            )
        }

        factory { EditorLibraryScreenBuilder() } bind ScreenBuilder::class
    }
