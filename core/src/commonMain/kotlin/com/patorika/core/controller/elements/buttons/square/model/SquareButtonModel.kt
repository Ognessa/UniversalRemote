package com.patorika.core.controller.elements.buttons.square.model

import com.patorika.core.controller.elements.buttons.config.ButtonConfigModel
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.config.NormalizedDisplay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("SquareButton")
data class SquareButtonModel(
    override val id: String = Uuid.Companion.random().toString(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val name: String = "Btn",
    val interactionConfig: ButtonConfigModel = ButtonConfigModel(),
) : ControllerModel() {
    override fun createElementWithNewId(): ControllerModel = this.copy(id = Uuid.Companion.random().toString())

    override fun getElementWithDefaultDisplayParameters(): ControllerModel = this.copy(displayParameters = NormalizedDisplay())
}
