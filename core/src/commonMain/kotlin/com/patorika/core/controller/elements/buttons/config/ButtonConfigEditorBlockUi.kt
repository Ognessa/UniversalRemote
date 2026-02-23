package com.patorika.core.controller.elements.buttons.config

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.square_button_edit_on_hold_label
import universalremote.core.generated.resources.square_button_edit_on_press_label
import universalremote.core.generated.resources.square_button_edit_on_release_label

@Composable
fun ColumnScope.ButtonConfigEditorBlockUi(
    config: ButtonConfigModel,
    onModified: (ButtonConfigModel) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onPress,
        onValueChange = { new -> onModified(config.copy(onPress = new)) },
        label = { Text(stringResource(Res.string.square_button_edit_on_press_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onHold,
        onValueChange = { new -> onModified(config.copy(onHold = new)) },
        label = { Text(stringResource(Res.string.square_button_edit_on_hold_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.onRelease,
        onValueChange = { new -> onModified(config.copy(onRelease = new)) },
        label = { Text(stringResource(Res.string.square_button_edit_on_release_label)) },
    )
}
