package com.patorika.feature_editor_presentation.model

import com.patorika.core.controller.model.ControllerModel

data class ControllerEditorScreenState(
    val isLibraryOpened: Boolean = false,
    val elements: List<ControllerModel> = emptyList(),
    val selectedElementId: String? = null,
)
