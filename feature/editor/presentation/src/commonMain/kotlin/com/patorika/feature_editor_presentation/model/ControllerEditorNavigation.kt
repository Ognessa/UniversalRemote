package com.patorika.feature_editor_presentation.model

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel

sealed interface ControllerEditorNavigation {
    data object Close : ControllerEditorNavigation

    data object OpenLibrary : ControllerEditorNavigation

    data class OpenSignalEditor(
        val model: ControllerElementModel,
    ) : ControllerEditorNavigation
}
