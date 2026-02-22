package com.patorika.core.ui.elements.canvas

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.model.controller.ControllerModel
import com.patorika.core.model.controller.ControllerRenderMode
import com.patorika.core.ui.elements.items.SliderUi
import com.patorika.core.ui.elements.items.SquareButtonUi
import com.patorika.core.ui.elements.items.XboxButtonClusterUi

@Composable
fun ControllerCanvas(
    modifier: Modifier = Modifier,
    list: List<ControllerModel>,
    selectedElementIndex: Int? = null,
    renderMode: ControllerRenderMode,
    onClick: (Int, ControllerModel) -> Unit,
    onModified: (Int, ControllerModel) -> Unit,
    onAction: (String) -> Unit = {},
) {
    list.forEachIndexed { index, element ->
        when (element) {
            is ControllerModel.SquareButton -> {
                SquareButtonUi(
                    modifier = modifier,
                    data = element,
                    isSelected = selectedElementIndex == index,
                    renderMode = renderMode,
                    onClick = { onClick(index, element) },
                    onModified = { data -> onModified(index, data) },
                    onAction = onAction,
                )
            }

            is ControllerModel.XboxButtonCluster -> {
                XboxButtonClusterUi(
                    modifier = modifier,
                    data = element,
                    isSelected = selectedElementIndex == index,
                    renderMode = renderMode,
                    onClick = { onClick(index, element) },
                    onModified = { data -> onModified(index, data) },
                    onAction = onAction,
                )
            }

            is ControllerModel.Slider -> {
                SliderUi(
                    modifier = modifier,
                    data = element,
                    isSelected = selectedElementIndex == index,
                    renderMode = renderMode,
                    onClick = { onClick(index, element) },
                    onModified = { data -> onModified(index, data) },
                    onAction = onAction,
                )
            }
        }
    }
}
