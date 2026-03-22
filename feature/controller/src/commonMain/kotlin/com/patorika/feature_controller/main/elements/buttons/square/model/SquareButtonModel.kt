package com.patorika.feature_controller.main.elements.buttons.square.model

import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.main.elements.basic.config.NormalizedDisplay
import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.elements.buttons.config.model.ButtonConfigModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("SquareButton")
data class SquareButtonModel(
    override val id: String = Uuid.random().toString(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val name: String = "Btn",
    val interactionConfig: ButtonConfigModel = ButtonConfigModel(),
) : ControllerElementModel() {
    override fun createElementWithNewId(): ControllerElementModel = this.copy(id = Uuid.random().toString())

    override fun getDefaultSize(): Size = Size(120f, 120f)

    override fun validate(): Boolean = true

    override fun changeDisplayParameters(params: NormalizedDisplay): ControllerElementModel = this.copy(displayParameters = params)

    override val jsonVersion: Int = 1
    override val serialName: String = serializer().descriptor.serialName
}
