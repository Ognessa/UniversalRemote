package com.patorika.core.controller.elements.slider.config

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.slider_config_edit_max_value_label
import universalremote.core.generated.resources.slider_config_edit_min_value_label
import universalremote.core.generated.resources.slider_config_edit_prefix_label
import universalremote.core.generated.resources.slider_config_edit_preview_label
import universalremote.core.generated.resources.slider_config_edit_step_value_label
import universalremote.core.generated.resources.slider_config_edit_suffix_label

@Composable
fun SliderConfigEditorBlockUi(
    config: SliderConfigModel,
    onModified: (SliderConfigModel) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.prefix,
        onValueChange = { new -> onModified(config.copy(prefix = new)) },
        label = { Text(stringResource(Res.string.slider_config_edit_prefix_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        value = config.min.toString(),
        onValueChange = { new -> onModified(config.copy(min = new.toFloat())) },
        label = { Text(stringResource(Res.string.slider_config_edit_min_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        value = config.max.toString(),
        onValueChange = { new -> onModified(config.copy(max = new.toFloat())) },
        label = { Text(stringResource(Res.string.slider_config_edit_max_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        value = config.step.toString(),
        onValueChange = { new -> onModified(config.copy(step = new.toFloat())) },
        label = { Text(stringResource(Res.string.slider_config_edit_step_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.suffix,
        onValueChange = { new -> onModified(config.copy(prefix = new)) },
        label = { Text(stringResource(Res.string.slider_config_edit_suffix_label)) },
    )

    if (config.isValid()) {
        val previewResult = "${config.prefix}${config.max}${config.suffix}"
        Text("${stringResource(Res.string.slider_config_edit_preview_label)} $previewResult")
    }
}
