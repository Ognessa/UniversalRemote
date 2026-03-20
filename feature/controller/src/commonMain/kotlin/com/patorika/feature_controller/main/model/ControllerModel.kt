package com.patorika.feature_controller.main.model

import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("ControllerModel")
data class ControllerModel(
    val id: String = Uuid.random().toString(),
    val name: String = "",
    val elements: List<ControllerElementModel> = emptyList(),
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
)
