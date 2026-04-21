package com.patorika.feature_bluetooth_presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesEvents
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesNavigation
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothDevicesBody
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothDevicesTopBar

@Composable
internal fun BluetoothDevicesScreen(
    viewModel: BluetoothDevicesViewModel,
    navigate: (BluetoothDevicesNavigation) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event -> navigate(event) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BluetoothDevicesTopBar(
                onEvent = viewModel::onEvent,
            )
        },
    ) { paddingValues ->
        PullToRefreshBox(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(BluetoothDevicesEvents.Refresh) },
        ) {
            BluetoothDevicesBody(
                modifier = Modifier.fillMaxSize(),
                devices = state.devices,
                onEvent = { viewModel.onEvent(it) },
            )
        }
    }
}
