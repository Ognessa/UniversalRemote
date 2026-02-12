package com.patorika.universalremote.feature.editor.main.model

import com.patorika.universalremote.core.model.controller.ControllerType

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
