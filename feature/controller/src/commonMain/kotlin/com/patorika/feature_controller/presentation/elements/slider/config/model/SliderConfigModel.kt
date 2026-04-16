package com.patorika.feature_controller.presentation.elements.slider.config.model

import com.patorika.core.provider.text.TextProvider
import com.patorika.feature_controller.presentation.elements.basic.config.InteractionConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import universalremote.feature_controller.generated.resources.Res
import universalremote.feature_controller.generated.resources.slider_config_edit_max_value_must_be_bigger_error
import universalremote.feature_controller.generated.resources.slider_config_edit_steps_amount_error
import universalremote.feature_controller.generated.resources.slider_config_edit_value_invalid_error

@Serializable
@SerialName("SliderConfig")
data class SliderConfigModel(
    val prefix: String = "slider",
    val suffix: String = ";",
    val min: String = "0.0",
    val max: String = "1.0",
    val stepsAmount: String = "1",
) : InteractionConfig {
    val minValue: Float = min.toFloatOrNull() ?: 0f
    val maxValue: Float = max.toFloatOrNull() ?: 0f
    val stepsAmountValue: Int = stepsAmount.toIntOrNull() ?: 0

    override fun validate(): Boolean = getErrorMessages().isEmpty()

    fun getErrorMessages(): List<SliderConfigErrorType> =
        mutableListOf<SliderConfigErrorType>().apply {
            if (min.isBlank() || min.toFloatOrNull() == null) {
                add(SliderConfigErrorType.Min(TextProvider.Res(Res.string.slider_config_edit_value_invalid_error)))
            }

            if (max.isBlank() || max.toFloatOrNull() == null) {
                add(SliderConfigErrorType.Max(TextProvider.Res(Res.string.slider_config_edit_value_invalid_error)))
            }

            if (maxValue <= minValue && min.toFloatOrNull() != null && max.toFloatOrNull() != null) {
                add(SliderConfigErrorType.Max(TextProvider.Res(Res.string.slider_config_edit_max_value_must_be_bigger_error)))
            }

            if (stepsAmount.isBlank() || stepsAmount.toIntOrNull() == null) {
                add(SliderConfigErrorType.Step(TextProvider.Res(Res.string.slider_config_edit_value_invalid_error)))
            }

            if (stepsAmountValue < 0 && stepsAmount.toIntOrNull() != null) {
                add(SliderConfigErrorType.Step(TextProvider.Res(Res.string.slider_config_edit_steps_amount_error)))
            }
        }
}
