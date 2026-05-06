package com.patorika.feature_bluetooth_presentation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.feature_bluetooth_api.BluetoothDevicesScreenBuilder
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesNavigation
import com.patorika.feature_bluetooth_presentation.ui.DevicePickerScreen
import com.patorika.feature_bluetooth_presentation.ui.DevicePickerViewModel
import org.koin.compose.viewmodel.koinViewModel

class BluetoothDevicesScreenBuilderImpl : BluetoothDevicesScreenBuilder {
    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(routeName) {
            DevicePickerScreen(
                viewModel = koinViewModel<DevicePickerViewModel>(),
                navigate = { handleNavigation(navController, it) },
            )
        }
    }

    private fun handleNavigation(
        navController: NavController,
        type: BluetoothDevicesNavigation,
    ) {
        when (type) {
            is BluetoothDevicesNavigation.Close -> navController.popBackStack()
        }
    }
}
