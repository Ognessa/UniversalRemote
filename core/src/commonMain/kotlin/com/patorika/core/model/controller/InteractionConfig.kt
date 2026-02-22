package com.patorika.core.model.controller

sealed interface InteractionConfig {
    data class Button(
        val onPress: String = "",
        val onHold: String = "",
        val onRelease: String = "",
    ) : InteractionConfig

    data class Slider(
        val prefix: String = "slider",
        val suffix: String = ";",
        val min: Float = 0f,
        val max: Float = 1f,
        val step: Float = 0.1f,
    ) : InteractionConfig
}

fun initBaseButtonSignal(text: String): InteractionConfig.Button =
    InteractionConfig.Button(
        onPress = text,
        onHold = text,
        onRelease = text,
    )
