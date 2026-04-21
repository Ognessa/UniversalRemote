package com.patorika.feature_playground_presentation.api

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.savedstate.read
import com.patorika.feature_bluetooth_api.BluetoothDevicesScreenBuilder
import com.patorika.feature_playground_api.api.PlaygroundScreenBuilder
import com.patorika.feature_playground_presentation.model.PlaygroundNavigationEvent
import com.patorika.feature_playground_presentation.ui.PlaygroundScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class PlaygroundScreenBuilderImpl(
    private val bluetoothDevicesScreenBuilder: BluetoothDevicesScreenBuilder,
) : PlaygroundScreenBuilder {
    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(
            route = "$routeName/{$CONTROLLER_ID_ARG}",
        ) { backStackEntry ->
            val id = backStackEntry.extractControllerIdArg()
            PlaygroundScreen(
                viewModel = koinViewModel { parametersOf(id) },
                navigate = { handleNavigation(it, navController) },
            )
        }
    }

    private fun NavBackStackEntry.extractControllerIdArg(): String =
        this.arguments?.read {
            getStringOrNull(CONTROLLER_ID_ARG)
        } ?: error("Missing nav arg: $CONTROLLER_ID_ARG")

    private fun handleNavigation(
        event: PlaygroundNavigationEvent,
        navController: NavController,
    ) {
        when (event) {
            is PlaygroundNavigationEvent.Close -> {
                navController.popBackStack()
            }

            is PlaygroundNavigationEvent.OpenDevicePicker -> {
                navController.navigate(bluetoothDevicesScreenBuilder.routeName)
            }
        }
    }

    companion object {
        const val CONTROLLER_ID_ARG = "CONTROLLER_ID_ARG"
    }
}
