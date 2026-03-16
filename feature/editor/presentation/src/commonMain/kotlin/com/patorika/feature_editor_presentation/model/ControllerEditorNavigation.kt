package com.patorika.feature_editor_presentation.model

import com.patorika.feature_controller.elements.basic.model.ControllerElementModel

sealed class ControllerEditorNavigation {
    data object OpenLibrary : ControllerEditorNavigation()

    data class OpenSignalEditor(
        val model: ControllerElementModel,
    ) : ControllerEditorNavigation()
}
