package com.patorika.feature_editor_presentation.model

import com.patorika.core.controller.model.ControllerModel

sealed class ControllerEditorUserEvent {
    data class ElementClicked(
        val id: String,
    ) : ControllerEditorUserEvent()

    data class ElementModified(
        val element: ControllerModel,
    ) : ControllerEditorUserEvent()

    data object ClearSelection : ControllerEditorUserEvent()
}
