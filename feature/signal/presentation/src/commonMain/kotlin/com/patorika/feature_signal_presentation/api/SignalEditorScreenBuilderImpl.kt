package com.patorika.feature_signal_presentation.api

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.elements.basic.serialization.controllerModelFromNavArg
import com.patorika.feature_signal_api.SignalEditorScreenBuilder
import com.patorika.feature_signal_presentation.model.SignalEditorScreenNavigation
import com.patorika.feature_signal_presentation.ui.SignalEditorScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class SignalEditorScreenBuilderImpl : SignalEditorScreenBuilder {
    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(
            route = "$routeName/{$EDITED_ELEMENT_ARG}",
            arguments = initArguments(),
        ) { backStackEntry ->
            val elementData = backStackEntry.encodeEditedElementArgument()

            SignalEditorScreen(
                viewModel = koinViewModel { parametersOf(elementData) },
                navigate = { type -> handleNavigation(type, navController) },
            )
        }
    }

    private fun NavBackStackEntry.encodeEditedElementArgument(): ControllerElementModel {
        val encoded =
            this.arguments?.read {
                getStringOrNull(EDITED_ELEMENT_ARG)
            } ?: error("Missing nav arg: $EDITED_ELEMENT_ARG")

        return controllerModelFromNavArg(encoded)
    }

    private fun initArguments() =
        listOf(
            navArgument(EDITED_ELEMENT_ARG) {
                type = NavType.StringType
                nullable = false
            },
        )

    private fun handleNavigation(
        type: SignalEditorScreenNavigation,
        navController: NavController,
    ) {
        when (type) {
            is SignalEditorScreenNavigation.Close -> navController.popBackStack()
        }
    }

    companion object {
        const val EDITED_ELEMENT_ARG = "EDITED_ELEMENT_ARG"
    }
}
