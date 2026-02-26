package com.patorika.core.controller.elements.slider.ui

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.basic.BasicElementUi
import com.patorika.core.controller.elements.slider.model.SliderModel
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.ControllerRenderMode

@Composable
fun SliderUi(
    modifier: Modifier = Modifier,
    data: SliderModel,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerModel) -> Unit = {},
    onAction: (String) -> Unit = {},
) {
    val prefix = data.interactionConfig.prefix
    val suffix = data.interactionConfig.suffix

    val max = data.interactionConfig.maxValue
    val min = data.interactionConfig.minValue
    val steps = data.interactionConfig.stepsAmountValue

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
                onValueChange = { newValue -> onModified(data.copy(currentValue = newValue)) },
                onValueChangeFinished = { onAction("$prefix${data.currentValue}$suffix") },
                valueRange = min..max,
                steps = steps,
            )
        },
    )
}
