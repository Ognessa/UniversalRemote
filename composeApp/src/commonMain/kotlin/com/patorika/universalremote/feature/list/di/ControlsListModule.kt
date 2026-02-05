package com.patorika.universalremote.feature.list.di

import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.list.ControlsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val controlsListModule =
    module {
        viewModel { ControlsListViewModel() }

        factory { ControlsListScreenBuilder() }
        factory<ScreenBuilder> { get<ControlsListScreenBuilder>() }
    }
