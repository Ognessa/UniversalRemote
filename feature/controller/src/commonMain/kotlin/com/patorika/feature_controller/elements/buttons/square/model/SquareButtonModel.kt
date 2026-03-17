package com.patorika.feature_controller.elements.buttons.square.model

import com.patorika.feature_controller.elements.basic.config.NormalizedDisplay
import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.elements.buttons.config.ButtonConfigModel
import com.patorika.feature_controller.main.model.ControllerOrientation
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
) : ControllerElementModel() {
    override fun createElementWithNewId(): ControllerElementModel = this.copy(id = Uuid.Companion.random().toString())

    override fun getElementWithDefaultDisplayParameters(): ControllerElementModel = this.copy(displayParameters = NormalizedDisplay())

    override fun validate(): Boolean = true

    override fun changeOrientation(orientation: ControllerOrientation): ControllerElementModel =
        this.copy(displayParameters = displayParameters.changeOrientation(orientation))

    override val jsonVersion: Int = 1
    override val serialName: String = serializer().descriptor.serialName
}
