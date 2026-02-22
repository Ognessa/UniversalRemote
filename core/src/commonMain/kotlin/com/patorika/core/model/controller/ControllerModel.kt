package com.patorika.core.model.controller

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class ControllerModel(
    open val id: String,
    open val displayParameters: NormalizedDisplay,
) {
    data class SquareButton(
        override val id: String = Uuid.random().toString(),
        override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
        val name: String = "Btn",
        val interactionConfig: InteractionConfig.Button = InteractionConfig.Button(),
    ) : ControllerModel(
            id = id,
            displayParameters = displayParameters,
        )

    data class XboxButtonCluster(
        override val id: String = Uuid.random().toString(),
        override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
        val nameA: String = "A",
        val nameB: String = "B",
        val nameX: String = "X",
        val nameY: String = "Y",
        val interactionConfigA: InteractionConfig.Button = initBaseButtonSignal("A"),
        val interactionConfigB: InteractionConfig.Button = initBaseButtonSignal("B"),
        val interactionConfigX: InteractionConfig.Button = initBaseButtonSignal("X"),
        val interactionConfigY: InteractionConfig.Button = initBaseButtonSignal("Y"),
    ) : ControllerModel(
            id = id,
            displayParameters = displayParameters,
        )

    data class Slider(
        override val id: String = Uuid.random().toString(),
        override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
        val name: String = "Slider",
        val interactionConfig: InteractionConfig.Slider = InteractionConfig.Slider(),
        val currentValue: Float = 0f,
    ) : ControllerModel(
            id = id,
            displayParameters = displayParameters,
        )
}

val defaultControllersList =
    listOf<ControllerModel>(
        ControllerModel.SquareButton(),
        ControllerModel.XboxButtonCluster(),
        ControllerModel.Slider(),
    )
