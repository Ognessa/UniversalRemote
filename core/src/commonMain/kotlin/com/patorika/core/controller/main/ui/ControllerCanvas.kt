package com.patorika.core.controller.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.elements.basic.model.ControllerRenderMode
import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.elements.buttons.square.ui.SquareButtonUi
import com.patorika.core.controller.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.core.controller.elements.buttons.xbox.ui.XboxButtonClusterUi
import com.patorika.core.controller.elements.slider.model.SliderModel
import com.patorika.core.controller.elements.slider.ui.SliderUi
import com.patorika.core.controller.main.model.ControllerOrientation

@Composable
fun ControllerCanvas(
    modifier: Modifier = Modifier,
    list: List<ControllerElementModel>,
    orientation: ControllerOrientation,
    selectedElementId: String? = null,
    renderMode: ControllerRenderMode,
    onClick: (ControllerElementModel) -> Unit,
    onModified: (ControllerElementModel) -> Unit,
    onAction: (String) -> Unit = {},
) {
    list.forEach { element ->
        when (element) {
            is SquareButtonModel -> {
                SquareButtonUi(
                    modifier = modifier,
                    data = element,
                    orientation = orientation,
                    isSelected = selectedElementId == element.id,
                    renderMode = renderMode,
                    onClick = { onClick(element) },
                    onModified = { data -> onModified(data) },
                    onAction = onAction,
                )
            }

            is XboxButtonClusterModel -> {
                XboxButtonClusterUi(
                    modifier = modifier,
                    data = element,
                    orientation = orientation,
                    isSelected = selectedElementId == element.id,
                    renderMode = renderMode,
                    onClick = { onClick(element) },
                    onModified = { data -> onModified(data) },
                    onAction = onAction,
                )
            }

            is SliderModel -> {
                SliderUi(
                    modifier = modifier,
                    data = element,
                    orientation = orientation,
                    isSelected = selectedElementId == element.id,
                    renderMode = renderMode,
                    onClick = { onClick(element) },
                    onModified = { data -> onModified(data) },
                    onAction = onAction,
                )
            }
        }
    }
}
