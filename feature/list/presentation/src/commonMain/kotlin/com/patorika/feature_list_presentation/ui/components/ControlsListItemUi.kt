package com.patorika.feature_list_presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import com.patorika.core.provider.text.TextProvider
import com.patorika.core.ui.menu.dropdown.CustomDropdownItemModel
import com.patorika.core.ui.menu.dropdown.CustomDropdownMenu
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_controller.main.elements.basic.model.ControllerRenderMode
import com.patorika.feature_controller.main.model.ControllerModel
import com.patorika.feature_controller.main.model.ControllerOrientation
import com.patorika.feature_controller.main.ui.ControllerCanvas
import com.patorika.feature_list_presentation.model.ControlsListEvents
import universalremote.feature_list_presentation.generated.resources.Res
import universalremote.feature_list_presentation.generated.resources.control_item_delete
import universalremote.feature_list_presentation.generated.resources.control_item_duplicate
import universalremote.feature_list_presentation.generated.resources.control_item_edit
import universalremote.feature_list_presentation.generated.resources.control_item_rename

@Composable
internal fun ControlsListItemUi(
    modifier: Modifier = Modifier,
    model: ControllerModel,
    onEvent: (ControlsListEvents) -> Unit,
) {
    CustomDropdownMenu(
        items =
            listOf(
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.control_item_rename),
                    onClick = { onEvent(ControlsListEvents.Item.Rename(model.id)) },
                ),
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.control_item_edit),
                    onClick = { onEvent(ControlsListEvents.Item.Edit(model.id)) },
                ),
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.control_item_duplicate),
                    onClick = { onEvent(ControlsListEvents.Item.Duplicate(model.id)) },
                ),
                CustomDropdownItemModel(
                    title = TextProvider.Res(Res.string.control_item_delete),
                    onClick = { onEvent(ControlsListEvents.Item.Delete(model.id)) },
                ),
            ),
    ) { onOpenMenu ->
        Card(
            modifier = modifier,
            border = CardDefaults.outlinedCardBorder(),
        ) {
            ControllerPreview(
                model = model,
                onLongPress = onOpenMenu,
            )

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(CoreDimens.current.standardContentPadding),
                text = model.name,
            )
        }
    }
}

@Composable
private fun ControllerPreview(
    model: ControllerModel,
    onLongPress: () -> Unit,
) {
    val rotation =
        when (model.orientation) {
            ControllerOrientation.PORTRAIT -> 0f
            ControllerOrientation.LANDSCAPE -> -90f
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(12f / 9f)
                .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        ControllerCanvas(
            modifier =
                Modifier
                    .rotateWithBounds(rotation)
                    .fillMaxHeight()
                    .aspectRatio(model.canvasRatio),
            list = model.elements,
            orientation = model.orientation,
            renderMode = ControllerRenderMode.ListPreview,
            onClick = {},
            onModified = {},
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = { onLongPress() })
                    },
        )
    }
}

fun Modifier.rotateWithBounds(degrees: Float): Modifier {
    if (degrees == 0f) return this

    return this
        .layout { measurable, constraints ->
            // Міняємо constraints місцями перед вимірюванням
            val rotatedConstraints =
                constraints.copy(
                    minWidth = constraints.minHeight,
                    maxWidth = constraints.maxHeight,
                    minHeight = constraints.minWidth,
                    maxHeight = constraints.maxWidth,
                )
            val placeable = measurable.measure(rotatedConstraints)

            // Layout розмір — поміняний місцями
            layout(placeable.height, placeable.width) {
                placeable.placeWithLayer(
                    x = -(placeable.width / 2 - placeable.height / 2),
                    y = -(placeable.height / 2 - placeable.width / 2),
                ) {
                    rotationZ = degrees
                }
            }
        }
}
