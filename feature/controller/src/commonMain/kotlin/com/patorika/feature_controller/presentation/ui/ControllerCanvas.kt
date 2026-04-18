package com.patorika.feature_controller.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.presentation.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.presentation.elements.buttons.square.ui.SquareButtonUi
import com.patorika.feature_controller.presentation.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.presentation.elements.buttons.xbox.ui.XboxButtonClusterUi
import com.patorika.feature_controller.presentation.elements.error.ElementRepresentationError
import com.patorika.feature_controller.presentation.elements.slider.model.SliderModel
import com.patorika.feature_controller.presentation.elements.slider.ui.SliderUi
import com.patorika.feature_controller.presentation.model.ControllerOrientation

@Composable
fun ControllerCanvas(
    modifier: Modifier = Modifier,
    list: List<ControllerElementModel>,
    orientation: ControllerOrientation,
    selectedElementId: String? = null,
    renderMode: ControllerRenderMode,
    onClick: (ControllerElementModel) -> Unit = {},
    onModified: (ControllerElementModel) -> Unit = {},
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

            else -> {
                ElementRepresentationError(
                    modifier = modifier,
                    data = element,
                    orientation = orientation,
                    isSelected = selectedElementId == element.id,
                    renderMode = renderMode,
                    onClick = { onClick(element) },
                    onModified = { data -> onModified(data) },
                )
            }
        }
    }
}
