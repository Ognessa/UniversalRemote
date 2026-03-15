package com.patorika.core.controller.elements.slider.model

import com.patorika.core.controller.elements.basic.config.NormalizedDisplay
import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.elements.slider.config.SliderConfigModel
import com.patorika.core.controller.main.model.ControllerOrientation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("Slider")
data class SliderModel(
    override val id: String = Uuid.Companion.random().toString(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val name: String = "Slider",
    val interactionConfig: SliderConfigModel = SliderConfigModel(),
    val currentValue: Float = 0f,
) : ControllerElementModel() {
    override fun createElementWithNewId(): ControllerElementModel = this.copy(id = Uuid.Companion.random().toString())

    override fun getElementWithDefaultDisplayParameters(): ControllerElementModel = this.copy(displayParameters = NormalizedDisplay())

    override fun validate(): Boolean = interactionConfig.validate()

    override fun changeOrientation(orientation: ControllerOrientation): ControllerElementModel =
        this.copy(displayParameters = displayParameters.changeOrientation(orientation))
}
