package com.patorika.feature_controller.data.entity

import com.patorika.feature_controller.data.database.GetAllControllers
import com.patorika.feature_controller.data.database.GetControllerById

data class FullControllerDao(
    val controllerId: String,
    val name: String,
    val orientation: String,
    val canvasRatio: Float,
    val elementId: String?,
    val parentId: String?,
    val type: String?,
    val jsonVersion: Long?,
    val json: String?,
)

fun GetAllControllers.mapToControllerDao(): FullControllerDao =
    FullControllerDao(
        controllerId = this.controllerId,
        name = this.name,
        orientation = this.orientation,
        canvasRatio = this.canvasRatio.toFloat(),
        elementId = this.elementId,
        parentId = this.parentId,
        type = this.type,
        jsonVersion = this.jsonVersion,
        json = this.json,
    )

fun GetControllerById.mapToControllerDao(): FullControllerDao =
    FullControllerDao(
        controllerId = this.controllerId,
        name = this.name,
        orientation = this.orientation,
        canvasRatio = this.canvasRatio.toFloat(),
        elementId = this.elementId,
        parentId = this.parentId,
        type = this.type,
        jsonVersion = this.jsonVersion,
        json = this.json,
    )
