package com.patorika.feature_editor_presentation.model

import com.patorika.core.model.controller.ControllerType

data class ControllerEditorScreenState(
    val isLibraryOpened: Boolean = false,
    val elements: List<ControllerType> = emptyList(),
    val selectedElementIndex: Int? = null,
)
