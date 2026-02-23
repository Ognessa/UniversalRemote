package com.patorika.core.controller.canvas

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.buttons.square.SquareButtonModel
import com.patorika.core.controller.elements.buttons.square.SquareButtonUi
import com.patorika.core.controller.elements.buttons.xbox.XboxButtonClusterModel
import com.patorika.core.controller.elements.buttons.xbox.XboxButtonClusterUi
import com.patorika.core.controller.elements.slider.SliderModel
import com.patorika.core.controller.elements.slider.SliderUi
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.ControllerRenderMode

@Composable
fun ControllerCanvas(
    modifier: Modifier = Modifier,
    list: List<ControllerModel>,
    selectedElementId: String? = null,
    renderMode: ControllerRenderMode,
    onClick: (ControllerModel) -> Unit,
    onModified: (ControllerModel) -> Unit,
    onAction: (String) -> Unit = {},
) {
    list.forEach { element ->
        when (element) {
            is SquareButtonModel -> {
                SquareButtonUi(
                    modifier = modifier,
                    data = element,
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
