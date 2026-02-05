package com.patorika.universalremote.feature.list

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
import com.patorika.universalremote.core.ui.CoreDimens
import com.patorika.universalremote.feature.list.components.ControlsListBody
import com.patorika.universalremote.feature.list.components.ControlsListTopBar
import com.patorika.universalremote.feature.list.components.CreateControllerButton
import com.patorika.universalremote.feature.list.model.ControlsListEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ControlsListScreen(viewModel: ControlsListViewModel) {
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
                onClick = { viewModel.onEvent(ControlsListEvents.CreateNewControl) },
            )
        }
    }
}
