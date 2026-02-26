package com.patorika.core.controller.elements.slider.config

import com.patorika.core.controller.model.config.InteractionConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

    override fun validate(): Boolean {
        val isMaxBigger = maxValue > minValue
        val isStepValid = stepsAmountValue >= 0 && stepsAmount.contains("[,.]".toRegex()).not()
        val valuesAreNotEmpty = min.isNotEmpty() && max.isNotEmpty() && stepsAmount.isNotEmpty()

        return isMaxBigger && isStepValid && valuesAreNotEmpty
    }
}
