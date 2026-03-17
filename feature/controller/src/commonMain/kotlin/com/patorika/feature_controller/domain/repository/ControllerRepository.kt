package com.patorika.feature_controller.domain.repository

import com.patorika.feature_controller.main.model.ControllerModel
import kotlinx.coroutines.flow.Flow

interface ControllerRepository {
    fun getAllControllers(): Flow<List<ControllerModel>>

    suspend fun getControllerById(id: String): ControllerModel?

    suspend fun insertController(model: ControllerModel)

    suspend fun removeController(id: String)
}
