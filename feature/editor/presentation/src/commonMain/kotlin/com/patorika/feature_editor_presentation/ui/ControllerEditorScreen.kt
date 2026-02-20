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
import com.patorika.core.model.controller.ControllerRenderMode
import com.patorika.core.ui.elements.canvas.ControllerCanvas
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
                onOrientationPressed = {
                    // todo
                },
                onPlusPressed = { navigate(ControllerEditorNavigation.OpenLibrary) },
                onSavePressed = {
                    // todo
                },
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
                selectedElementIndex = state.selectedElementIndex,
                renderMode = ControllerRenderMode.Editor,
                onClick = { index, _ ->
                    viewModel.onEvent(
                        ControllerEditorUserEvent.ElementClicked(index = index),
                    )
                },
                onModified = { index, element ->
                    viewModel.onEvent(
                        ControllerEditorUserEvent.ElementModified(
                            index = index,
                            element = element,
                        ),
                    )
                },
            )
        }
    }
}
