package com.patorika.feature_controller.data.repository

import com.patorika.feature_controller.data.database.Database
import com.patorika.feature_controller.data.database.DatabaseDriverFactory
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.main.model.ControllerModel
import kotlinx.coroutines.flow.Flow

class ControllerRepositoryImpl(
    databaseDriverFactory: DatabaseDriverFactory,
) : ControllerRepository {
    private val database = Database(databaseDriverFactory)

    override fun getAllControllers(): Flow<List<ControllerModel>> = database.getAllControllers()

    override suspend fun getControllerById(id: String): ControllerModel? = database.getControllerById(id)

    override suspend fun insertController(model: ControllerModel) = database.insertController(model)

    override suspend fun removeController(id: String) = database.removeController(id)
}
