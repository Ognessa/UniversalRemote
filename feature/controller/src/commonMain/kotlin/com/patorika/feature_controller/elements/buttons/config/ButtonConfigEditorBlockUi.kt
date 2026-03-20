package com.patorika.feature_controller.elements.buttons.config

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        onValueChange = { new -> onModified(config.copy(onPress = new)) },
        label = { Text(stringResource(Res.string.button_config_edit_on_press_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onHold,
        onValueChange = { new -> onModified(config.copy(onHold = new)) },
        label = { Text(stringResource(Res.string.button_config_edit_on_hold_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onRelease,
        onValueChange = { new -> onModified(config.copy(onRelease = new)) },
        label = { Text(stringResource(Res.string.button_config_edit_on_release_label)) },
    )
}
