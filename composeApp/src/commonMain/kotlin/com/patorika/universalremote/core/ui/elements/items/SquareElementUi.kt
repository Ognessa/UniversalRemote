package com.patorika.universalremote.core.ui.elements.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patorika.universalremote.core.model.controller.ControllerRenderMode
import com.patorika.universalremote.core.model.controller.ControllerType
import com.patorika.universalremote.core.ui.elements.items.basic.BasicElementUi

@Composable
fun SquareElementUi(
    modifier: Modifier = Modifier,
    data: ControllerType.Square,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerType) -> Unit = {},
) {
    BasicElementUi(
        modifier = modifier,
        parameters = data.displayParameters,
        isSelected = isSelected,
        renderMode = renderMode,
        onClick = onClick,
        onParametersModified = { params ->
            onModified(data.copy(displayParameters = params))
        },
    ) {
        drawRect(Color.Blue)
    }
}
