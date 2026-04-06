package com.patorika.feature_controller.presentation.elements.slider.model

import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.presentation.elements.basic.config.NormalizedDisplay
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.elements.slider.config.model.SliderConfigModel
import com.patorika.feature_controller.presentation.ext.generateControllerId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Slider")
data class SliderModel(
    override val id: String = generateControllerId(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val name: String = "Slider",
    val interactionConfig: SliderConfigModel = SliderConfigModel(),
    val currentValue: Float = 0f,
) : ControllerElementModel() {
    override fun createElementWithNewId(): ControllerElementModel = this.copy(id = generateControllerId())

    override fun getDefaultSize(): Size = Size(180f, 60f)

    override fun validate(): Boolean = interactionConfig.validate()

    override fun changeDisplayParameters(params: NormalizedDisplay): ControllerElementModel = this.copy(displayParameters = params)

    override val jsonVersion: Int = 1
    override val serialName: String = serializer().descriptor.serialName
}
