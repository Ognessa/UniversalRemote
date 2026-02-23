package com.patorika.feature_editor_presentation.model

import com.patorika.core.controller.model.ControllerModel

sealed class ControllerEditorNavigation {
    data object OpenLibrary : ControllerEditorNavigation()

    data class OpenSignalEditor(
        val model: ControllerModel,
    ) : ControllerEditorNavigation()
}
