package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_controller.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.main.model.ControllerModel
import com.patorika.feature_controller.main.model.ControllerOrientation
import com.patorika.feature_controller.main.ui.ControllerCanvas

@Composable
internal fun ControlsListItemUi(
    modifier: Modifier = Modifier,
    model: ControllerModel,
) {
    Card(modifier = modifier) {
//        ControllerPreview(
//            model = model,
//        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(CoreDimens.current.standardContentPadding),
            text = model.id,
        )
    }
}

@Composable
private fun ControllerPreview(model: ControllerModel) {
    val aspectRatio =
        when (model.orientation) {
            ControllerOrientation.PORTRAIT -> 9f / 16f
            ControllerOrientation.LANDSCAPE -> 16f / 9f
        }

    val rotation =
        when (model.orientation) {
            ControllerOrientation.PORTRAIT -> 0f
            ControllerOrientation.LANDSCAPE -> -90f
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
    ) {
        ControllerCanvas(
            modifier =
                Modifier
                    .rotate(rotation)
                    .align(Alignment.Center),
            list = model.elements,
            orientation = model.orientation,
            renderMode = ControllerRenderMode.ListPreview,
            onClick = {},
            onModified = {},
        )
    }
}
