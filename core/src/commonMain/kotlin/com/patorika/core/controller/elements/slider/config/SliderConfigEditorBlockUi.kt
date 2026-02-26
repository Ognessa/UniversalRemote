package com.patorika.core.controller.elements.slider.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.slider_config_edit_max_value_label
import universalremote.core.generated.resources.slider_config_edit_max_value_must_be_bigger_error
import universalremote.core.generated.resources.slider_config_edit_min_value_label
import universalremote.core.generated.resources.slider_config_edit_prefix_label
import universalremote.core.generated.resources.slider_config_edit_preview_error
import universalremote.core.generated.resources.slider_config_edit_preview_label
import universalremote.core.generated.resources.slider_config_edit_step_value_label
import universalremote.core.generated.resources.slider_config_edit_steps_amount_error
import universalremote.core.generated.resources.slider_config_edit_steps_amount_hint
import universalremote.core.generated.resources.slider_config_edit_steps_amount_integer_error
import universalremote.core.generated.resources.slider_config_edit_suffix_label
import universalremote.core.generated.resources.slider_config_edit_value_empty_error

@Composable
fun SliderConfigEditorBlockUi(
    config: SliderConfigModel,
    onModified: (SliderConfigModel) -> Unit,
) {
    SliderConfigValidator(config)

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.prefix,
        onValueChange = { new -> onModified(config.copy(prefix = new)) },
        label = { Text(stringResource(Res.string.slider_config_edit_prefix_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        value = config.min,
        onValueChange = { new -> onModified(config.copy(min = new)) },
        supportingText = {
            if (config.min.isEmpty()) {
                Text(
                    text = stringResource(Res.string.slider_config_edit_value_empty_error),
                )
            }
        },
        label = { Text(stringResource(Res.string.slider_config_edit_min_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        value = config.max,
        onValueChange = { new -> onModified(config.copy(max = new)) },
        supportingText = {
            Column {
                if (config.max.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.slider_config_edit_value_empty_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                if (config.maxValue <= config.minValue) {
                    Text(
                        text = stringResource(Res.string.slider_config_edit_max_value_must_be_bigger_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
        label = { Text(stringResource(Res.string.slider_config_edit_max_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = config.stepsAmount,
        onValueChange = { new -> onModified(config.copy(stepsAmount = new)) },
        supportingText = {
            Column {
                if (config.stepsAmount.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.slider_config_edit_value_empty_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                if (config.stepsAmountValue < 0) {
                    Text(
                        text = stringResource(Res.string.slider_config_edit_steps_amount_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                if (config.stepsAmount.contains("[,.]".toRegex())) {
                    Text(
                        text = stringResource(Res.string.slider_config_edit_steps_amount_integer_error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                Text(stringResource(Res.string.slider_config_edit_steps_amount_hint))
            }
        },
        label = { Text(stringResource(Res.string.slider_config_edit_step_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.suffix,
        onValueChange = { new -> onModified(config.copy(suffix = new)) },
        label = { Text(stringResource(Res.string.slider_config_edit_suffix_label)) },
    )
}

@Composable
private fun SliderConfigValidator(config: SliderConfigModel) {
    if (config.validate()) {
        val previewResult = "${config.prefix}${config.maxValue}${config.suffix}"
        Text("${stringResource(Res.string.slider_config_edit_preview_label)} \"$previewResult\"")
    } else {
        Text(
            text = stringResource(Res.string.slider_config_edit_preview_error),
            color = MaterialTheme.colorScheme.error,
        )
    }
}
