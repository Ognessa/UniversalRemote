package com.patorika.feature_controller.presentation.elements.error

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.presentation.elements.basic.ui.BasicElementUi
import com.patorika.feature_controller.presentation.model.ControllerOrientation
import org.jetbrains.compose.resources.painterResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.ic_warning

@Composable
fun ElementRepresentationError(
    modifier: Modifier = Modifier,
    data: ControllerElementModel,
    orientation: ControllerOrientation,
    isSelected: Boolean,
    renderMode: ControllerRenderMode,
    onClick: () -> Unit = {},
    onModified: (ControllerElementModel) -> Unit = {},
) {
    BasicElementUi(
        modifier = modifier,
        parameters = data.displayParameters,
        defaultSize = data.getDefaultSize(),
        isSelected = isSelected,
        renderMode = renderMode,
        orientation = orientation,
        onClick = onClick,
        onParametersModified = { params -> onModified(data.changeDisplayParameters(params)) },
        onComposable = { sizePx ->
            Image(
                modifier = Modifier.matchParentSize(),
                painter = painterResource(Res.drawable.ic_warning),
                contentDescription = null,
            )
        },
    )
}
