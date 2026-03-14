package com.patorika.feature_editor_presentation.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patorika.core.controller.elements.basic.model.ControllerRenderMode
import com.patorika.core.controller.main.ui.ControllerCanvas
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import com.patorika.feature_editor_presentation.ui.components.EditorToolbar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ControllerEditorScreen(
    viewModel: ControllerEditorViewModel,
    navigate: (ControllerEditorNavigation) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EditorToolbar(
                isElementSelected = state.selectedElementId != null,
                orientation = state.orientation,
                onOrientationPressed = { viewModel.onEvent(ControllerEditorUserEvent.OrientationChanged) },
                onPlusPressed = { navigate(ControllerEditorNavigation.OpenLibrary) },
                onEditPressed = {
                    // TODO replace with cleaner version
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
        Box(
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
