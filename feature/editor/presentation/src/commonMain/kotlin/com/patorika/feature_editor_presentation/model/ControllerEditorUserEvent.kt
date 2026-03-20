package com.patorika.feature_editor_presentation.model

import com.patorika.feature_controller.elements.basic.model.ControllerElementModel

sealed class ControllerEditorUserEvent {
    data class ElementClicked(
        val id: String,
    ) : ControllerEditorUserEvent()

    data class ElementModified(
        val element: ControllerElementModel,
    ) : ControllerEditorUserEvent()

    data object ClearSelection : ControllerEditorUserEvent()

    data object OrientationChanged : ControllerEditorUserEvent()

    data object Save : ControllerEditorUserEvent()
}
