package com.patorika.feature_editor_presentation.model

import com.patorika.core.model.controller.ControllerType

sealed class ControllerEditorUserEvent {
    data class ElementClicked(
        val index: Int,
    ) : ControllerEditorUserEvent()

    data class ElementModified(
        val index: Int,
        val element: ControllerType,
    ) : ControllerEditorUserEvent()

    data object ClearSelection : ControllerEditorUserEvent()
}
