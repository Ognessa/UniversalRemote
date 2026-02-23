package com.patorika.core.controller.elements.buttons.xbox

import com.patorika.core.controller.elements.buttons.config.ButtonConfigModel
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.controller.model.config.NormalizedDisplay
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
) : ControllerModel() {
    override fun createElementWithNewId(): ControllerModel = this.copy(id = Uuid.random().toString())

    override fun getElementWithDefaultDisplayParameters(): ControllerModel = this.copy(displayParameters = NormalizedDisplay())
}
