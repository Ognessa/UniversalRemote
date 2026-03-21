package com.patorika.feature_controller.elements.buttons.square.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.feature_controller.elements.buttons.config.ui.ButtonConfigEditorBlockUi
import com.patorika.feature_controller.elements.buttons.square.model.SquareButtonModel
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_controller.generated.resources.Res
import universalremote.feature_controller.generated.resources.square_button_edit_name_label

@Composable
fun ColumnScope.SquareButtonEditorBlock(
    data: SquareButtonModel,
    onModified: (SquareButtonModel) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = data.name,
        onValueChange = { new -> onModified(data.copy(name = new)) },
        label = { Text(stringResource(Res.string.square_button_edit_name_label)) },
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    ButtonConfigEditorBlockUi(
        config = data.interactionConfig,
        onModified = { new -> onModified(data.copy(interactionConfig = new)) },
    )
}
