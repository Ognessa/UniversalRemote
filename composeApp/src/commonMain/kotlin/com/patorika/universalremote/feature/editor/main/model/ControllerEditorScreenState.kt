package com.patorika.universalremote.feature.editor.main.model

import com.patorika.universalremote.core.model.controller.ControllerType

data class ControllerEditorScreenState(
    val isLibraryOpened: Boolean = false,
    val elements: List<ControllerType> = emptyList(),
    val selectedElementIndex: Int? = null,
)
