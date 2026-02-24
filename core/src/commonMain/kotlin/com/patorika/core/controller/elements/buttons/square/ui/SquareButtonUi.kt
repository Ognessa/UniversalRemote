package com.patorika.core.controller.elements.buttons.square.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.basic.BasicElementUi
import com.patorika.core.controller.elements.buttons.square.model.SquareButtonModel
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.ControllerRenderMode
import com.patorika.core.ui.components.buttons.HoldableButton
import com.patorika.core.ui.theme.CoreDimens

@Composable
fun SquareButtonUi(
    modifier: Modifier = Modifier,
    data: SquareButtonModel,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerModel) -> Unit = {},
    onAction: (String) -> Unit = {},
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
