package com.patorika.feature_library_presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_controller.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.elements.defaultControllersList
import com.patorika.feature_controller.main.model.ControllerOrientation
import com.patorika.feature_controller.main.ui.ControllerCanvas
import com.patorika.feature_library_presentation.model.EditorLibraryNavigation
import com.patorika.feature_library_presentation.model.EditorLibraryUserEvents
import com.patorika.feature_library_presentation.ui.components.EditorLibraryToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorLibraryScreen(
    viewModel: EditorLibraryViewModel,
    navigate: (EditorLibraryNavigation) -> Unit,
) {
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.navigationEvent.collect { event -> navigate(event) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EditorLibraryToolbar(
                onClosePressed = { navigate(EditorLibraryNavigation.CloseLibrary) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState),
        ) {
            Spacer(
                modifier =
                    Modifier
                        .height(CoreDimens.current.standardContentPadding)
                        .fillMaxWidth(),
            )

            FlowRow(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = CoreDimens.current.standardContentPadding),
                horizontalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
                verticalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
            ) {
                ControllerCanvas(
                    list = defaultControllersList,
                    orientation = ControllerOrientation.PORTRAIT,
                    renderMode = ControllerRenderMode.Preview,
                    onClick = { element ->
                        viewModel.onUserEvent(
                            EditorLibraryUserEvents.ElementSelected(element),
                        )
                    },
                    onModified = { _ -> },
                )
            }

            Spacer(
                modifier =
                    Modifier
                        .height(CoreDimens.current.standardContentPadding)
                        .fillMaxWidth(),
            )
        }
    }
}
