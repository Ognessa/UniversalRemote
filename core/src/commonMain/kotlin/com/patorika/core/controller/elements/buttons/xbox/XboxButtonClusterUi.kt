package com.patorika.core.controller.elements.buttons.xbox

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
import androidx.compose.ui.unit.dp
import com.patorika.core.controller.elements.basic.BasicElementUi
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.ControllerRenderMode
import com.patorika.core.ui.components.buttons.HoldableButton
import com.patorika.core.ui.ext.pxToDp

@Composable
fun XboxButtonClusterUi(
    modifier: Modifier = Modifier,
    data: XboxButtonClusterModel,
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
                    modifier = Modifier.size((sizePx / 3).pxToDp()).align(Alignment.TopCenter),
                    name = data.nameY,
                    interactionConfig = data.interactionConfigY,
                    onAction = onAction,
                )

                HoldableButton(
                    modifier = Modifier.size((sizePx / 3).pxToDp()).align(Alignment.CenterStart),
                    name = data.nameX,
                    interactionConfig = data.interactionConfigX,
                    onAction = onAction,
                )

                HoldableButton(
                    modifier = Modifier.size((sizePx / 3).pxToDp()).align(Alignment.CenterEnd),
                    name = data.nameB,
                    interactionConfig = data.interactionConfigB,
                    onAction = onAction,
                )

                HoldableButton(
                    modifier = Modifier.size((sizePx / 3).pxToDp()).align(Alignment.BottomCenter),
                    name = data.nameA,
                    interactionConfig = data.interactionConfigA,
                    onAction = onAction,
                )
            }
        },
    )
}
