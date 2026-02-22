package com.patorika.core.ui.elements.items

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.model.controller.ControllerModel
import com.patorika.core.model.controller.ControllerRenderMode
import com.patorika.core.ui.elements.items.basic.BasicElementUi
import kotlin.math.roundToInt

@Composable
fun SliderUi(
    modifier: Modifier = Modifier,
    data: ControllerModel.Slider,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerModel) -> Unit = {},
    onAction: (String) -> Unit = {},
) {
    val prefix = data.interactionConfig.prefix
    val suffix = data.interactionConfig.suffix

    val max = data.interactionConfig.max
    val min = data.interactionConfig.min
    val step = data.interactionConfig.step

    BasicElementUi(
        modifier = modifier,
        parameters = data.displayParameters,
        isSelected = isSelected,
        renderMode = renderMode,
        onClick = onClick,
        onParametersModified = { params ->
            onModified(data.copy(displayParameters = params))
        },
        onComposable = { sizePx ->
            Slider(
                modifier = Modifier.matchParentSize(),
                enabled = renderMode == ControllerRenderMode.Action,
                value = data.currentValue,
                onValueChange = { newValue ->
                    onModified(data.copy(currentValue = newValue))
                },
                onValueChangeFinished = {
                    onAction("$prefix${data.currentValue}$suffix")
                },
                valueRange = min..max,
                steps = ((max - min) / step).roundToInt(),
            )
        },
    )
}
