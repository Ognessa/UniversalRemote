package com.patorika.feature_list_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.feature_list_presentation.api.ControlsListScreenBuilderImpl
import com.patorika.feature_list_presentation.ui.ControlsListViewModel
import com.patorika.feature_list_presentation.usecase.GetAllControllersUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val controlsListModule =
    module {
        viewModel {
            ControlsListViewModel(
                appNotificationManager = get<AppNotificationManager>(),
                getAllControllersUseCase = get<GetAllControllersUseCase>(),
            )
        }

        factory<ControlsListScreenBuilder> {
            ControlsListScreenBuilderImpl(
                editorScreenBuilder = get<ControllerEditorScreenBuilder>(),
            )
        } bind ScreenBuilder::class

        factory {
            GetAllControllersUseCase(
                repository = get<ControllerRepository>(),
            )
        }
    }
