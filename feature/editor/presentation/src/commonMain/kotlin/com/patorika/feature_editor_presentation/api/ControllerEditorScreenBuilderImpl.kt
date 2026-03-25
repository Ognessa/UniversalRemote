package com.patorika.feature_editor_presentation.api

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.patorika.feature_controller.main.elements.basic.serialization.toNavArg
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.model.ControllerEditorParams
import com.patorika.feature_editor_presentation.ui.ControllerEditorScreen
import com.patorika.feature_library_api.EditorLibraryScreenBuilder
import com.patorika.feature_signal_api.SignalEditorScreenBuilder
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class ControllerEditorScreenBuilderImpl(
    private val editorLibraryScreenBuilder: EditorLibraryScreenBuilder,
    private val signalEditorScreenBuilder: SignalEditorScreenBuilder,
) : ControllerEditorScreenBuilder {
    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(
            route = "$routeName/{$CONTROLLER_ID_ARG}",
            arguments = initArguments(),
        ) { backStackEntry ->
            val controllerId = backStackEntry.extractControllerIdArgument()
            ControllerEditorScreen(
                viewModel = koinViewModel { parametersOf(ControllerEditorParams(controllerId)) },
                navigate = { handleNavigation(navController, it) },
            )
        }
    }

    private fun initArguments() =
        listOf(
            navArgument(CONTROLLER_ID_ARG) {
                type = NavType.StringType
                nullable = true
            },
        )

    private fun NavBackStackEntry.extractControllerIdArgument(): String? =
        this.arguments?.read {
            getStringOrNull(CONTROLLER_ID_ARG)
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

            is ControllerEditorNavigation.Close -> {
                navController.popBackStack()
            }
        }
    }

    companion object {
        const val CONTROLLER_ID_ARG = "CONTROLLER_ID_ARG"
    }
}
