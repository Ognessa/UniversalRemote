package com.patorika.core.controller.elements.slider

import com.patorika.core.controller.elements.slider.config.SliderConfigModel
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.config.NormalizedDisplay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("Slider")
data class SliderModel(
    override val id: String = Uuid.random().toString(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val name: String = "Slider",
    val interactionConfig: SliderConfigModel = SliderConfigModel(),
    val currentValue: Float = 0f,
) : ControllerModel() {
    override fun createElementWithNewId(): ControllerModel = this.copy(id = Uuid.random().toString())

    override fun getElementWithDefaultDisplayParameters(): ControllerModel = this.copy(displayParameters = NormalizedDisplay())
}
