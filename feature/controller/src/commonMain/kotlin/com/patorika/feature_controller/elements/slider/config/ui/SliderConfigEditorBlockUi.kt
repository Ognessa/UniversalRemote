package com.patorika.feature_controller.elements.slider.config.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.patorika.core.provider.TextProvider
import com.patorika.core.provider.getString
import com.patorika.feature_controller.elements.slider.config.model.SliderConfigErrorType
import com.patorika.feature_controller.elements.slider.config.model.SliderConfigModel
import org.jetbrains.compose.resources.stringResource
import universalremote.feature_controller.generated.resources.Res
import universalremote.feature_controller.generated.resources.slider_config_edit_max_value_label
import universalremote.feature_controller.generated.resources.slider_config_edit_min_value_label
import universalremote.feature_controller.generated.resources.slider_config_edit_prefix_label
import universalremote.feature_controller.generated.resources.slider_config_edit_preview_error
import universalremote.feature_controller.generated.resources.slider_config_edit_preview_label
import universalremote.feature_controller.generated.resources.slider_config_edit_step_value_label
import universalremote.feature_controller.generated.resources.slider_config_edit_steps_amount_hint
import universalremote.feature_controller.generated.resources.slider_config_edit_suffix_label

@Composable
fun SliderConfigEditorBlockUi(
    config: SliderConfigModel,
    onModified: (SliderConfigModel) -> Unit,
) {
    val errors = config.getErrorMessages()

    val minErrors = errors.filterIsInstance<SliderConfigErrorType.Min>().map { it.message }
    val maxErrors = errors.filterIsInstance<SliderConfigErrorType.Max>().map { it.message }
    val stepErrors = errors.filterIsInstance<SliderConfigErrorType.Step>().map { it.message }

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
        onValueChange = { new -> onModified(config.copy(min = new.filterDecimalInput())) },
        supportingText = { ErrorBlock(errorsList = minErrors) },
        isError = minErrors.isNotEmpty(),
        label = { Text(stringResource(Res.string.slider_config_edit_min_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        value = config.max,
        onValueChange = { new -> onModified(config.copy(max = new.filterDecimalInput())) },
        supportingText = { ErrorBlock(errorsList = maxErrors) },
        isError = maxErrors.isNotEmpty(),
        label = { Text(stringResource(Res.string.slider_config_edit_max_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = config.stepsAmount,
        onValueChange = { new -> onModified(config.copy(stepsAmount = new.filterIntInput())) },
        supportingText = {
            ErrorBlock(
                errorsList = stepErrors,
                hint = stringResource(Res.string.slider_config_edit_steps_amount_hint),
            )
        },
        isError = stepErrors.isNotEmpty(),
        label = { Text(stringResource(Res.string.slider_config_edit_step_value_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = config.suffix,
        onValueChange = { new -> onModified(config.copy(suffix = new)) },
        label = { Text(stringResource(Res.string.slider_config_edit_suffix_label)) },
    )
}

private const val NUMBERS_BEFORE_DOT = 10
private const val NUMBERS_AFTER_DOT = 3

private fun String.filterDecimalInput(): String {
    var result =
        this
            .replace(',', '.')
            .filter { it.isDigit() || it == '.' || it == '-' }

    // only 1 dot
    val dotIndex = result.indexOf('.')
    if (dotIndex != -1) {
        result = result.substring(0, dotIndex + 1) +
            result.substring(dotIndex + 1).replace(".", "")
    }

    // limit before and after dot
    val parts = result.split(".")
    val intPart = parts[0].take(NUMBERS_BEFORE_DOT)
    val fracPart = parts.getOrNull(1)?.take(NUMBERS_AFTER_DOT)

    return if (fracPart != null) "$intPart.$fracPart" else intPart
}

private fun String.filterIntInput(): String = this.filter { it.isDigit() }.take(NUMBERS_BEFORE_DOT)

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

@Composable
private fun ErrorBlock(
    errorsList: List<TextProvider>,
    hint: String? = null,
) {
    Column {
        errorsList.forEach { error ->
            Text(
                text = error.getString(),
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (hint != null) {
            Text(hint)
        }
    }
}
