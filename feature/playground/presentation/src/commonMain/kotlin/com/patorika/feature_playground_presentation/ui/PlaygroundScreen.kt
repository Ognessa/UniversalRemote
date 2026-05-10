package com.patorika.feature_playground_presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.ui.loader.CustomLoader
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.presentation.ui.ControllerCanvas
import com.patorika.feature_playground_presentation.Res
import com.patorika.feature_playground_presentation.controller_loading_error
import com.patorika.feature_playground_presentation.model.PlaygroundNavigationEvent
import com.patorika.feature_playground_presentation.model.PlaygroundUserEvent
import com.patorika.feature_playground_presentation.ui.components.BluetoothConnectionStatusBanner
import com.patorika.feature_playground_presentation.ui.components.PlaygroundToolbar
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaygroundScreen(
    viewModel: PlaygroundViewModel,
    navigate: (PlaygroundNavigationEvent) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collect { event -> navigate(event) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PlaygroundToolbar(
                connectedDeviceName = state.bluetoothState.connectedDevice?.name,
                deviceConnectionStatus = state.bluetoothState.connectionState,
                onEvent = viewModel::onEvent,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            BluetoothConnectionStatusBanner(
                state = state,
            )

            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoading.not()) {
                    state.controller?.let { model ->
                        ControllerCanvas(
                            modifier = Modifier.fillMaxSize(),
                            list = model.elements,
                            orientation = model.orientation,
                            selectedElementId = null,
                            renderMode = ControllerRenderMode.Action,
                            onModified = { viewModel.onEvent(PlaygroundUserEvent.ElementModified(it)) },
                            onAction = { viewModel.onEvent(PlaygroundUserEvent.ElementAction(it)) },
                        )
                    } ?: Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.controller_loading_error),
                        textAlign = TextAlign.Center,
                    )
                }

                CustomLoader(
                    modifier = Modifier.align(Alignment.Center),
                    isLoading = state.isLoading,
                )
            }
        }
    }
}
