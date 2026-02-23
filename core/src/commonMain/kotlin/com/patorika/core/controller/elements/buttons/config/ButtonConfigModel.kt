package com.patorika.core.controller.elements.buttons.config

import com.patorika.core.controller.model.config.InteractionConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Button")
data class ButtonConfigModel(
    val onPress: String = "",
    val onHold: String = "",
    val onRelease: String = "",
) : InteractionConfig {
    companion object {
        fun initBasic(text: String): ButtonConfigModel =
            ButtonConfigModel(
                onPress = text,
                onHold = text,
                onRelease = text,
            )
    }
}
