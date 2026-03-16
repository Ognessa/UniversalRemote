package com.patorika.feature_controller.elements.buttons.square.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.elements.basic.ui.BasicElementUi
import com.patorika.feature_controller.elements.buttons.components.HoldableButton
import com.patorika.feature_controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.feature_controller.main.model.ControllerOrientation

@Composable
fun SquareButtonUi(
    modifier: Modifier = Modifier,
    data: SquareButtonModel,
    orientation: ControllerOrientation,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerElementModel) -> Unit = {},
    onAction: (String) -> Unit = {},
) {
    BasicElementUi(
        modifier = modifier,
        parameters = data.displayParameters,
        isSelected = isSelected,
        renderMode = renderMode,
        orientation = orientation,
        onClick = onClick,
        onParametersModified = { params ->
            onModified(data.copy(displayParameters = params))
        },
        onComposable = { sizePx ->
            HoldableButton(
                modifier = Modifier.matchParentSize(),
                name = data.name,
                shape = RoundedCornerShape(CoreDimens.current.standardCornerSize),
                interactionConfig = data.interactionConfig,
                onAction = onAction,
            )
        },
    )
}
