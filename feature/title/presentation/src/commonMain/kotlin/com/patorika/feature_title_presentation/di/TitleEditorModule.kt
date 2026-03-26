package com.patorika.feature_title_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.feature_title_api.TitleEditorDialogBuilder
import com.patorika.feature_title_api.TitleEditorNavArgs
import com.patorika.feature_title_presentation.api.TitleEditorDialogBuilderImpl
import com.patorika.feature_title_presentation.ui.TitleEditorViewModel
import com.patorika.feature_title_presentation.useCase.SaveControllerUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val titleEditorModule =
    module {
        viewModel {
            TitleEditorViewModel(
                args = get<TitleEditorNavArgs>(),
                appNotificationManager = get<AppNotificationManager>(),
                saveControllerUseCase = get<SaveControllerUseCase>(),
            )
        }

        factory<TitleEditorDialogBuilder> {
            TitleEditorDialogBuilderImpl(
                controllerListScreenBuilder = { get<ControlsListScreenBuilder>() },
            )
        } bind ScreenBuilder::class

        factory {
            SaveControllerUseCase(
                repository = get<ControllerRepository>(),
            )
        }
    }
