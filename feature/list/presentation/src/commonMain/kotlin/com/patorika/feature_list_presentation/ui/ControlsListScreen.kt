package com.patorika.feature_list_presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_list_presentation.model.ControlsListEvents
import com.patorika.feature_list_presentation.model.ControlsListNavigation
import com.patorika.feature_list_presentation.ui.components.ControlsListBody
import com.patorika.feature_list_presentation.ui.components.ControlsListTopBar
import com.patorika.feature_list_presentation.ui.components.CreateControllerButton

@Composable
internal fun ControlsListScreen(
    viewModel: ControlsListViewModel,
    navigate: (ControlsListNavigation) -> Unit,
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
        topBar = { ControlsListTopBar() },
    ) { paddingValues ->
        PullToRefreshBox(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(ControlsListEvents.Refresh) },
        ) {
            ControlsListBody(
                modifier = Modifier.fillMaxSize(),
                list = state.controllersList,
                onEvent = { viewModel.onEvent(it) },
            )

            CreateControllerButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(CoreDimens.current.standardContentPadding),
                onClick = { viewModel.onEvent(ControlsListEvents.CreateNew) },
            )
        }
    }
}
