package com.patorika.feature_editor_presentation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.core.controller.elements.basic.serialization.toNavArg
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.ui.ControllerEditorScreen
import com.patorika.feature_library_api.EditorLibraryScreenBuilder
import com.patorika.feature_signal_api.SignalEditorScreenBuilder
import org.koin.compose.viewmodel.koinViewModel

class ControllerEditorScreenBuilderImpl(
    private val editorLibraryScreenBuilder: EditorLibraryScreenBuilder,
    private val signalEditorScreenBuilder: SignalEditorScreenBuilder,
) : ControllerEditorScreenBuilder {
    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(routeName) {
            ControllerEditorScreen(
                viewModel = koinViewModel(),
                navigate = { handleNavigation(navController, it) },
            )
        }
    }

    private fun handleNavigation(
        navController: NavController,
        type: ControllerEditorNavigation,
    ) {
        when (type) {
            is ControllerEditorNavigation.OpenLibrary -> {
                navController.navigate(editorLibraryScreenBuilder.routeName)
            }

            is ControllerEditorNavigation.OpenSignalEditor -> {
                navController.navigate(
                    route = "${signalEditorScreenBuilder.routeName}/${type.model.toNavArg()}",
                )
            }
        }
    }
}
