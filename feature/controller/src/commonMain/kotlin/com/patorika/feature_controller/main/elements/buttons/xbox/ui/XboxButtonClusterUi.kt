package com.patorika.feature_controller.main.elements.buttons.xbox.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.patorika.core.ui.ext.pxToDp
import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.main.elements.basic.ui.BasicElementUi
import com.patorika.feature_controller.main.elements.buttons.components.HoldableButton
import com.patorika.feature_controller.main.elements.buttons.xbox.model.XboxButtonClusterModel
import com.patorika.feature_controller.main.model.ControllerOrientation

@Composable
fun XboxButtonClusterUi(
    modifier: Modifier = Modifier,
    data: XboxButtonClusterModel,
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
        defaultSize = data.getDefaultSize(),
        isSelected = isSelected,
        renderMode = renderMode,
        orientation = orientation,
        onClick = onClick,
        onParametersModified = { params ->
            onModified(data.copy(displayParameters = params))
        },
        onComposable = { sizePx ->
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                        ).border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        ).padding(8.dp),
            ) {
                HoldableButton(
                    modifier = Modifier.setXboxButtonSize(sizePx).align(Alignment.TopCenter),
                    name = data.nameY,
                    interactionConfig = data.interactionConfigY,
                    onAction = onAction,
                )

                HoldableButton(
                    modifier = Modifier.setXboxButtonSize(sizePx).align(Alignment.CenterStart),
                    name = data.nameX,
                    interactionConfig = data.interactionConfigX,
                    onAction = onAction,
                )

                HoldableButton(
                    modifier = Modifier.setXboxButtonSize(sizePx).align(Alignment.CenterEnd),
                    name = data.nameB,
                    interactionConfig = data.interactionConfigB,
                    onAction = onAction,
                )

                HoldableButton(
                    modifier = Modifier.setXboxButtonSize(sizePx).align(Alignment.BottomCenter),
                    name = data.nameA,
                    interactionConfig = data.interactionConfigA,
                    onAction = onAction,
                )
            }
        },
    )
}

@Composable
private fun Modifier.setXboxButtonSize(sizePx: Size) =
    this.size(
        width = (sizePx.width / 3).pxToDp(),
        height = (sizePx.height / 3).pxToDp(),
    )
