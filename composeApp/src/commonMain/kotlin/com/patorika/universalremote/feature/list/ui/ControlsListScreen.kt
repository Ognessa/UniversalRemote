package com.patorika.universalremote.feature.list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patorika.universalremote.core.ui.theme.CoreDimens
import com.patorika.universalremote.feature.list.model.ControlsListEvents
import com.patorika.universalremote.feature.list.model.ControlsListNavigation
import com.patorika.universalremote.feature.list.ui.components.ControlsListBody
import com.patorika.universalremote.feature.list.ui.components.ControlsListTopBar
import com.patorika.universalremote.feature.list.ui.components.CreateControllerButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ControlsListScreen(
    viewModel: ControlsListViewModel,
    navigate: (ControlsListNavigation) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ControlsListTopBar() },
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(ControlsListEvents.Refresh) },
        ) {
            ControlsListBody(
                modifier = Modifier.fillMaxSize(),
                list = state.controllersList,
            )

            CreateControllerButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(CoreDimens.current.standardContentPadding),
                onClick = { navigate(ControlsListNavigation.OpenEditor) },
            )
        }
    }
}
