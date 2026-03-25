package com.patorika.feature_controller.main.model

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.ext.generateControllerId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ControllerModel")
data class ControllerModel(
    val id: String = generateControllerId(),
    val name: String = "",
    val canvasRatio: Float = 1f,
    val elements: List<ControllerElementModel> = emptyList(),
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
)
