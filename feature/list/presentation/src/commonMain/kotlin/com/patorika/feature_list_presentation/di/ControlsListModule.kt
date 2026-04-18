package com.patorika.feature_list_presentation.di

import com.patorika.core.navigation.ScreenBuilder
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.feature_list_presentation.api.ControlsListScreenBuilderImpl
import com.patorika.feature_list_presentation.ui.ControlsListViewModel
import com.patorika.feature_list_presentation.usecase.DeleteControllerUseCase
import com.patorika.feature_list_presentation.usecase.DuplicateControllerUseCase
import com.patorika.feature_list_presentation.usecase.GetAllControllersUseCase
import com.patorika.feature_playground_api.api.PlaygroundScreenBuilder
import com.patorika.feature_title_api.TitleEditorDialogBuilder
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val controlsListModule =
    module {
        viewModel {
            ControlsListViewModel(
                appNotificationManager = get<AppNotificationManager>(),
                getAllControllersUseCase = get<GetAllControllersUseCase>(),
                duplicateControllerUseCase = get<DuplicateControllerUseCase>(),
                deleteControllerUseCase = get<DeleteControllerUseCase>(),
            )
        }

        factory<ControlsListScreenBuilder> {
            ControlsListScreenBuilderImpl(
                editorScreenBuilder = get<ControllerEditorScreenBuilder>(),
                titleEditorDialogBuilder = { get<TitleEditorDialogBuilder>() },
                playgroundScreenBuilder = get<PlaygroundScreenBuilder>(),
            )
        } bind ScreenBuilder::class

        factory {
            GetAllControllersUseCase(
                repository = get<ControllerRepository>(),
            )
        }

        factory {
            DuplicateControllerUseCase(
                repository = get<ControllerRepository>(),
            )
        }

        factory {
            DeleteControllerUseCase(
                repository = get<ControllerRepository>(),
            )
        }
    }
