package com.patorika.core.controller.elements.buttons.square.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.elements.basic.model.ControllerRenderMode
import com.patorika.core.controller.elements.basic.ui.BasicElementUi
import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.main.model.ControllerOrientation
import com.patorika.core.ui.components.buttons.HoldableButton
import com.patorika.core.ui.theme.CoreDimens

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
