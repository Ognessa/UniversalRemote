package com.patorika.feature_controller.presentation.elements.slider.ui

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.presentation.elements.basic.ui.BasicElementUi
import com.patorika.feature_controller.presentation.elements.slider.model.SliderModel
import com.patorika.feature_controller.presentation.model.ControllerOrientation

@Composable
fun SliderUi(
    modifier: Modifier = Modifier,
    data: SliderModel,
    orientation: ControllerOrientation,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerElementModel) -> Unit = {},
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
        defaultSize = data.getDefaultSize(),
        isSelected = isSelected,
        renderMode = renderMode,
        orientation = orientation,
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
