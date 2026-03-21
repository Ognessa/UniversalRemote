package com.patorika.feature_controller.elements.buttons.xbox.model

import androidx.compose.ui.geometry.Size
import com.patorika.feature_controller.elements.basic.config.NormalizedDisplay
import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.elements.buttons.config.ButtonConfigModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("XboxButtonCluster")
data class XboxButtonClusterModel(
    override val id: String = Uuid.random().toString(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val nameA: String = "A",
    val nameB: String = "B",
    val nameX: String = "X",
    val nameY: String = "Y",
    val interactionConfigA: ButtonConfigModel = ButtonConfigModel.initBasic("A"),
    val interactionConfigB: ButtonConfigModel = ButtonConfigModel.initBasic("B"),
    val interactionConfigX: ButtonConfigModel = ButtonConfigModel.initBasic("X"),
    val interactionConfigY: ButtonConfigModel = ButtonConfigModel.initBasic("Y"),
) : ControllerElementModel() {
    override fun createElementWithNewId(): ControllerElementModel = this.copy(id = Uuid.random().toString())

    override fun getDefaultSize(): Size = Size(120f, 120f)

    override fun validate(): Boolean = true

    override fun changeDisplayParameters(params: NormalizedDisplay): ControllerElementModel = this.copy(displayParameters = params)

    override val jsonVersion: Int = 1
    override val serialName: String = serializer().descriptor.serialName
}
