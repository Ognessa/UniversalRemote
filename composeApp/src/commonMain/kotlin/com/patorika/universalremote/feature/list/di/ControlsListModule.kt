package com.patorika.universalremote.feature.list.di

import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.editor.main.di.ControllerEditorScreenBuilder
import com.patorika.universalremote.feature.list.ui.ControlsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val controlsListModule =
    module {
        viewModel { ControlsListViewModel() }

        factory {
            ControlsListScreenBuilder(
                editorScreenBuilder = get<ControllerEditorScreenBuilder>(),
            )
        } bind ScreenBuilder::class
    }
