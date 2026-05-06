package com.patorika.feature_bluetooth_presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesNavigation
import com.patorika.feature_bluetooth_presentation.permission.BluetoothPermissionRequest
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothDevicesTopBar
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothDisabledContent
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothPermissionDeniedContent
import com.patorika.feature_bluetooth_presentation.ui.tabs.DevicePickerBody

@Composable
internal fun DevicePickerScreen(
    viewModel: DevicePickerViewModel,
    navigate: (BluetoothDevicesNavigation) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var permissionGranted by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { navigate(it) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BluetoothDevicesTopBar(onEvent = viewModel::onEvent)
        },
    ) { paddingValues ->
        when {
            permissionGranted == null -> {
                BluetoothPermissionRequest { granted ->
                    permissionGranted = granted
                }
            }

            permissionGranted == false -> {
                BluetoothPermissionDeniedContent(modifier = Modifier.padding(paddingValues))
            }

            state.isBluetoothEnabled.not() -> {
                BluetoothDisabledContent()
            }

            else -> {
                DevicePickerBody(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    state = state,
                    onEvent = viewModel::onEvent,
                )
            }
        }
    }
}
