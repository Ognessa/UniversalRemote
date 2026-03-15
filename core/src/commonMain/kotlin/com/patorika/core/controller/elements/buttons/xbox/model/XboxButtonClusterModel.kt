package com.patorika.core.controller.elements.buttons.xbox.model

import com.patorika.core.controller.elements.basic.config.NormalizedDisplay
import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.elements.buttons.config.ButtonConfigModel
import com.patorika.core.controller.main.model.ControllerOrientation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("XboxButtonCluster")
data class XboxButtonClusterModel(
    override val id: String = Uuid.Companion.random().toString(),
    override val displayParameters: NormalizedDisplay = NormalizedDisplay(),
    val nameA: String = "A",
    val nameB: String = "B",
    val nameX: String = "X",
    val nameY: String = "Y",
    val interactionConfigA: ButtonConfigModel = ButtonConfigModel.Companion.initBasic("A"),
    val interactionConfigB: ButtonConfigModel = ButtonConfigModel.Companion.initBasic("B"),
    val interactionConfigX: ButtonConfigModel = ButtonConfigModel.Companion.initBasic("X"),
    val interactionConfigY: ButtonConfigModel = ButtonConfigModel.Companion.initBasic("Y"),
) : ControllerElementModel() {
    override fun createElementWithNewId(): ControllerElementModel = this.copy(id = Uuid.Companion.random().toString())

    override fun getElementWithDefaultDisplayParameters(): ControllerElementModel = this.copy(displayParameters = NormalizedDisplay())

    override fun validate(): Boolean = true

    override fun changeOrientation(orientation: ControllerOrientation): ControllerElementModel =
        this.copy(displayParameters = displayParameters.changeOrientation(orientation))
}
