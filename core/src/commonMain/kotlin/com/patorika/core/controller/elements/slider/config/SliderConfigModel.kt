package com.patorika.core.controller.elements.slider.config

import com.patorika.core.controller.model.config.InteractionConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Slider")
data class SliderConfigModel(
    val prefix: String = "slider",
    val suffix: String = ";",
    val min: Float = 0f,
    val max: Float = 1f,
    val step: Float = 0.1f,
) : InteractionConfig
