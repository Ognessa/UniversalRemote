package com.patorika.feature_controller.presentation.elements.buttons.config.model

import com.patorika.feature_controller.presentation.elements.basic.config.InteractionConfig
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
                onPress = "$text pressed",
                onHold = "",
                onRelease = "$text released",
            )
    }
}
