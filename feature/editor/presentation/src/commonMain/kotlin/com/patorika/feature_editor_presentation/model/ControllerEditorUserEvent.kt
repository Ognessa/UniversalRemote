package com.patorika.feature_editor_presentation.model

import com.patorika.core.model.controller.ControllerModel

sealed class ControllerEditorUserEvent {
    data class ElementClicked(
        val index: Int,
    ) : ControllerEditorUserEvent()

    data class ElementModified(
        val index: Int,
        val element: ControllerModel,
    ) : ControllerEditorUserEvent()

    data object ClearSelection : ControllerEditorUserEvent()
}
