package com.patorika.feature_editor_presentation.model

import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.main.model.ControllerOrientation

data class ControllerEditorScreenState(
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
    val elements: List<ControllerElementModel> = emptyList(),
    val selectedElementId: String? = null,
)
