package com.patorika.feature_editor_presentation.model

import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.model.ControllerOrientation

data class ControllerEditorScreenState(
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
    val elements: List<ControllerElementModel> = emptyList(),
    val selectedElementId: String? = null,
)
