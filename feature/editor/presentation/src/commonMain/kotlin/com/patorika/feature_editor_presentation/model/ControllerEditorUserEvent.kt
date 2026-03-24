package com.patorika.feature_editor_presentation.model

import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel

sealed interface ControllerEditorUserEvent {
    data object ClearSelection : ControllerEditorUserEvent

    data object OrientationChanged : ControllerEditorUserEvent

    data object OpenLibrary : ControllerEditorUserEvent

    data object Save : ControllerEditorUserEvent

    data class CanvasSizeChanged(
        val size: Size,
    ) : ControllerEditorUserEvent

    sealed interface ElementAction : ControllerEditorUserEvent {
        data class Clicked(
            val id: String,
        ) : ElementAction

        data class Modified(
            val element: ControllerElementModel,
        ) : ElementAction

        data class OpenSignalEditor(
            val id: String,
        ) : ElementAction

        data class Duplicate(
            val id: String,
        ) : ElementAction

        data class Delete(
            val id: String,
        ) : ElementAction
    }
}
