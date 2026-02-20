package com.patorika.core.ui.elements.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patorika.core.model.controller.ControllerRenderMode
import com.patorika.core.model.controller.ControllerType
import com.patorika.core.ui.elements.items.basic.BasicElementUi

@Composable
fun CircleElementUi(
    modifier: Modifier = Modifier,
    data: ControllerType.Circle,
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
        drawCircle(Color.Blue)
    }
}
