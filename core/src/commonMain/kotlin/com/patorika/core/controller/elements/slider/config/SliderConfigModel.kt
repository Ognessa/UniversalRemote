package com.patorika.core.controller.elements.slider.config

import com.patorika.core.controller.model.config.InteractionConfig
import com.patorika.core.provider.TextProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import universalremote.core.generated.resources.Res
import universalremote.core.generated.resources.slider_config_edit_max_value_must_be_bigger_error
import universalremote.core.generated.resources.slider_config_edit_steps_amount_error
import universalremote.core.generated.resources.slider_config_edit_steps_amount_integer_error
import universalremote.core.generated.resources.slider_config_edit_value_empty_error

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
            if (min.isBlank()) {
                add(SliderConfigErrorType.Min(TextProvider.Res(Res.string.slider_config_edit_value_empty_error)))
            }

            if (max.isBlank()) {
                add(SliderConfigErrorType.Max(TextProvider.Res(Res.string.slider_config_edit_value_empty_error)))
            }

            if (maxValue <= minValue) {
                add(SliderConfigErrorType.Max(TextProvider.Res(Res.string.slider_config_edit_max_value_must_be_bigger_error)))
            }

            if (stepsAmount.isBlank()) {
                add(SliderConfigErrorType.Step(TextProvider.Res(Res.string.slider_config_edit_value_empty_error)))
            }

            if (stepsAmountValue < 0) {
                add(SliderConfigErrorType.Step(TextProvider.Res(Res.string.slider_config_edit_steps_amount_error)))
            }

            if (stepsAmount.contains("[,.]".toRegex())) {
                add(SliderConfigErrorType.Step(TextProvider.Res(Res.string.slider_config_edit_steps_amount_integer_error)))
            }
        }
}
