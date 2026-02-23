package com.patorika.core.controller.elements.buttons.square

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.buttons.config.ButtonConfigEditorBlockUi
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.square_button_edit_name_label

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
