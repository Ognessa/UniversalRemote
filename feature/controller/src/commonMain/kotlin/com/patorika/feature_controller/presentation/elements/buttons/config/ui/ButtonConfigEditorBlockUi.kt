package com.patorika.feature_controller.presentation.elements.buttons.config.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.presentation.constants.ElementsConstants
import com.patorika.feature_controller.presentation.elements.buttons.config.model.ButtonConfigModel
import com.patorika.feature_controller.presentation.ext.limitEditorText
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_controller.generated.resources.Res
import universalremote.feature_controller.generated.resources.button_config_edit_on_hold_label
import universalremote.feature_controller.generated.resources.button_config_edit_on_press_label
import universalremote.feature_controller.generated.resources.button_config_edit_on_release_label

@Composable
fun ColumnScope.ButtonConfigEditorBlockUi(
    config: ButtonConfigModel,
    onModified: (ButtonConfigModel) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onPress,
        onValueChange = { new -> onModified(config.copy(onPress = new.limitEditorText())) },
        label = { Text(stringResource(Res.string.button_config_edit_on_press_label)) },
        singleLine = ElementsConstants.SINGLE_LINE,
        maxLines = ElementsConstants.DEFAULT_LINES_LIMIT,
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onHold,
        onValueChange = { new -> onModified(config.copy(onHold = new.limitEditorText())) },
        label = { Text(stringResource(Res.string.button_config_edit_on_hold_label)) },
        singleLine = ElementsConstants.SINGLE_LINE,
        maxLines = ElementsConstants.DEFAULT_LINES_LIMIT,
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onRelease,
        onValueChange = { new -> onModified(config.copy(onRelease = new.limitEditorText())) },
        label = { Text(stringResource(Res.string.button_config_edit_on_release_label)) },
        singleLine = ElementsConstants.SINGLE_LINE,
        maxLines = ElementsConstants.DEFAULT_LINES_LIMIT,
    )
}
