package com.patorika.core.controller.main.model

import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
@SerialName("ControllerModel")
data class ControllerModel(
    val id: String = Uuid.Companion.random().toString(),
    val elements: List<ControllerElementModel> = emptyList(),
    val orientation: ControllerOrientation = ControllerOrientation.PORTRAIT,
)
