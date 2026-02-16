package com.patorika.universalremote.core.ui.elements.canvas

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.universalremote.core.model.controller.ControllerRenderMode
import com.patorika.universalremote.core.model.controller.ControllerType
import com.patorika.universalremote.core.ui.elements.items.CircleElementUi
import com.patorika.universalremote.core.ui.elements.items.SquareElementUi

@Composable
fun ControllerCanvas(
    modifier: Modifier = Modifier,
    list: List<ControllerType>,
    selectedElementIndex: Int? = null,
    renderMode: ControllerRenderMode,
    onClick: (Int, ControllerType) -> Unit,
    onModified: (Int, ControllerType) -> Unit,
) {
    list.forEachIndexed { index, element ->
        when (element) {
            is ControllerType.Square -> {
                SquareElementUi(
                    modifier = modifier,
                    data = element,
                    isSelected = selectedElementIndex == index,
                    renderMode = renderMode,
                    onClick = { onClick(index, element) },
                    onModified = { data -> onModified(index, data) },
                )
            }

            is ControllerType.Circle -> {
                CircleElementUi(
                    modifier = modifier,
                    data = element,
                    isSelected = selectedElementIndex == index,
                    renderMode = renderMode,
                    onClick = { onClick(index, element) },
                    onModified = { data -> onModified(index, data) },
                )
            }
        }
    }
}
