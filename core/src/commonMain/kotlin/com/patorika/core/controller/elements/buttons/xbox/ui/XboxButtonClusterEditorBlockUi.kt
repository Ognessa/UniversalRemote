package com.patorika.core.controller.elements.buttons.xbox.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.controller.elements.buttons.config.ButtonConfigEditorBlockUi
import com.patorika.core.controller.elements.buttons.config.ButtonConfigModel
import com.patorika.core.controller.elements.buttons.xbox.model.XboxButtonClusterModel
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.xbox_button_cluster_edit_a_block_name
import universalremote.core.generated.resources.xbox_button_cluster_edit_b_block_name
import universalremote.core.generated.resources.xbox_button_cluster_edit_button_name
import universalremote.core.generated.resources.xbox_button_cluster_edit_x_block_name
import universalremote.core.generated.resources.xbox_button_cluster_edit_y_block_name

@Composable
fun ColumnScope.XboxButtonClusterEditorBlockUi(
    data: XboxButtonClusterModel,
    onModified: (XboxButtonClusterModel) -> Unit,
) {
    SingleButtonEditingBlock(
        title = stringResource(Res.string.xbox_button_cluster_edit_a_block_name),
        name = data.nameA,
        interactionConfig = data.interactionConfigA,
        onNameModified = { new -> onModified(data.copy(nameA = new)) },
        onInteractionConfigModified = { new -> onModified(data.copy(interactionConfigA = new)) },
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    SingleButtonEditingBlock(
        title = stringResource(Res.string.xbox_button_cluster_edit_b_block_name),
        name = data.nameB,
        interactionConfig = data.interactionConfigB,
        onNameModified = { new -> onModified(data.copy(nameB = new)) },
        onInteractionConfigModified = { new -> onModified(data.copy(interactionConfigB = new)) },
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    SingleButtonEditingBlock(
        title = stringResource(Res.string.xbox_button_cluster_edit_x_block_name),
        name = data.nameX,
        interactionConfig = data.interactionConfigX,
        onNameModified = { new -> onModified(data.copy(nameX = new)) },
        onInteractionConfigModified = { new -> onModified(data.copy(interactionConfigX = new)) },
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    SingleButtonEditingBlock(
        title = stringResource(Res.string.xbox_button_cluster_edit_y_block_name),
        name = data.nameY,
        interactionConfig = data.interactionConfigY,
        onNameModified = { new -> onModified(data.copy(nameY = new)) },
        onInteractionConfigModified = { new -> onModified(data.copy(interactionConfigY = new)) },
    )
}

@Composable
private fun ColumnScope.SingleButtonEditingBlock(
    title: String,
    name: String,
    interactionConfig: ButtonConfigModel,
    onNameModified: (String) -> Unit,
    onInteractionConfigModified: (ButtonConfigModel) -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = { new -> onNameModified(new) },
        label = { Text(stringResource(Res.string.xbox_button_cluster_edit_button_name)) },
    )

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    ButtonConfigEditorBlockUi(
        config = interactionConfig,
        onModified = { new -> onInteractionConfigModified(new) },
    )
}
