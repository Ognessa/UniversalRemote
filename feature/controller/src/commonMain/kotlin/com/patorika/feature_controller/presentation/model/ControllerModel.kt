package com.patorika.feature_controller.presentation.model

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.ext.generateControllerId
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ControllerModel")
data class ControllerModel(
    val id: String = generateControllerId(),
    val name: String = "",
    val canvasRatio: Float = 1f,
    val elements: List<@Polymorphic ControllerElementModel> = emptyList(),
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
)
