package com.patorika.feature_editor_presentation.model

import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.model.ControllerOrientation

data class ControllerEditorScreenState(
    val canvasSizeDp: Size = Size(300f, 600f),
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
    val elements: List<ControllerElementModel> = emptyList(),
    val selectedElementId: String? = null,
)
