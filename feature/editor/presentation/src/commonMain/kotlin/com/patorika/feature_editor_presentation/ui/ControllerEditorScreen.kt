package com.patorika.feature_editor_presentation.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.ui.ext.pxToDp
import com.patorika.feature_controller.main.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.main.ui.ControllerCanvas
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import com.patorika.feature_editor_presentation.ui.components.EditorToolbar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ControllerEditorScreen(
    viewModel: ControllerEditorViewModel,
    navigate: (ControllerEditorNavigation) -> Unit,
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
            EditorToolbar(
                isElementSelected = state.selectedElementId != null,
                orientation = state.orientation,
                onOrientationPressed = { viewModel.onEvent(ControllerEditorUserEvent.OrientationChanged) },
                onPlusPressed = { navigate(ControllerEditorNavigation.OpenLibrary) },
                onEditPressed = {
                    // TODO replace with context menu
                    state.elements
                        .firstOrNull {
                            state.selectedElementId.orEmpty() == it.id
                        }?.let {
                            navigate(ControllerEditorNavigation.OpenSignalEditor(it))
                        }
                },
                onSavePressed = { viewModel.onEvent(ControllerEditorUserEvent.Save) },
            )
        },
    ) { innerPadding ->
        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            viewModel.onEvent(ControllerEditorUserEvent.ClearSelection)
                        }
                    },
        ) {
            ListenToCanvasSizeChanges(
                onSizeDpChanged = { size ->
                    viewModel.onEvent(ControllerEditorUserEvent.CanvasSizeChanged(size))
                },
            )

            ControllerCanvas(
                modifier = Modifier.fillMaxSize(),
                list = state.elements,
                orientation = state.orientation,
                selectedElementId = state.selectedElementId,
                renderMode = ControllerRenderMode.Editor,
                onClick = { model ->
                    viewModel.onEvent(
                        ControllerEditorUserEvent.ElementClicked(model.id),
                    )
                },
                onModified = { model ->
                    viewModel.onEvent(
                        ControllerEditorUserEvent.ElementModified(element = model),
                    )
                },
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.ListenToCanvasSizeChanges(onSizeDpChanged: (Size) -> Unit) {
    val containerWidthDp =
        constraints.maxWidth
            .toFloat()
            .pxToDp()
            .value

    val containerHeightDp =
        constraints.maxHeight
            .toFloat()
            .pxToDp()
            .value

    LaunchedEffect(maxWidth, maxHeight) {
        onSizeDpChanged(Size(containerWidthDp, containerHeightDp))
    }
}
