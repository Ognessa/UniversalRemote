package com.patorika.core.ui.elements.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.model.controller.ControllerModel
import com.patorika.core.model.controller.ControllerRenderMode
import com.patorika.core.ui.components.buttons.HoldableButton
import com.patorika.core.ui.elements.items.basic.BasicElementUi
import com.patorika.core.ui.theme.CoreDimens

@Composable
fun SquareButtonUi(
    modifier: Modifier = Modifier,
    data: ControllerModel.SquareButton,
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
