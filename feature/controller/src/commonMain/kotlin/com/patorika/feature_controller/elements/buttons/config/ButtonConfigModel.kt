package com.patorika.feature_controller.elements.buttons.config

import com.patorika.feature_controller.elements.basic.config.InteractionConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ButtonConfig")
data class ButtonConfigModel(
    val onPress: String = "",
    val onHold: String = "",
    val onRelease: String = "",
) : InteractionConfig {
    override fun validate(): Boolean = true

    companion object {
        fun initBasic(text: String): ButtonConfigModel =
            ButtonConfigModel(
                onPress = text,
                onHold = text,
                onRelease = text,
            )
    }
}
