package com.patorika.feature_editor_presentation.model

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.model.ControllerModel

sealed interface ControllerEditorNavigation {
    data object Close : ControllerEditorNavigation

    data object OpenLibrary : ControllerEditorNavigation

    data class OpenSignalEditor(
        val model: ControllerElementModel,
    ) : ControllerEditorNavigation

    data class OpenTitleEditor(
        val model: ControllerModel,
    ) : ControllerEditorNavigation
}
