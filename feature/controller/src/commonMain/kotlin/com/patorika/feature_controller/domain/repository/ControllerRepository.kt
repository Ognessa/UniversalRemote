package com.patorika.feature_controller.domain.repository

import com.patorika.feature_controller.presentation.model.ControllerModel
import kotlinx.coroutines.flow.Flow

interface ControllerRepository {
    fun getAllControllers(): Flow<Result<List<ControllerModel>>>

    suspend fun getControllerById(id: String): Result<ControllerModel>

    suspend fun insertController(model: ControllerModel): Result<Unit>

    suspend fun removeController(id: String): Result<Unit>
}
