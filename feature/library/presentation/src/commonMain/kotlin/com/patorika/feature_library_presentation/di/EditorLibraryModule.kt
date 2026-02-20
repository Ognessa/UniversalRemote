package com.patorika.feature_library_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_library_api.EditorLibraryScreenBuilder
import com.patorika.feature_library_presentation.api.EditorLibraryScreenBuilderImpl
import com.patorika.feature_library_presentation.ui.EditorLibraryViewModel
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

        factory<EditorLibraryScreenBuilder> {
            EditorLibraryScreenBuilderImpl()
        } bind ScreenBuilder::class
    }
