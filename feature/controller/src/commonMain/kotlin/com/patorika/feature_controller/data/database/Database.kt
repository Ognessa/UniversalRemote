package com.patorika.feature_controller.data.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.patorika.feature_controller.data.entity.FullControllerDao
import com.patorika.feature_controller.data.entity.mapToControllerDao
import com.patorika.feature_controller.main.elements.basic.serialization.decodeToControllerElementModel
import com.patorika.feature_controller.main.elements.basic.serialization.encodeToString
import com.patorika.feature_controller.main.model.ControllerModel
import com.patorika.feature_controller.main.model.ControllerOrientation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class Database(
    databaseDriverFactory: DatabaseDriverFactory,
) {
    private val database = ControllerDatabase.Companion(databaseDriverFactory.createDriver())
    private val dbQuery = database.controllerDatabaseQueries

    fun getAllControllers(): Flow<List<ControllerModel>> =
        dbQuery
            .getAllControllers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list
                    .map { item -> item.mapToControllerDao() }
                    .mapControllers()
            }

    suspend fun getControllerById(id: String): ControllerModel? =
        withContext(Dispatchers.IO) {
            dbQuery
                .getControllerById(id)
                .executeAsList()
                .map { it.mapToControllerDao() }
                .mapControllers()
                .firstOrNull()
        }

    suspend fun insertController(model: ControllerModel) {
        withContext(Dispatchers.IO) {
            dbQuery.transaction {
                dbQuery.insertController(
                    id = model.id,
                    name = model.name,
                    orientation = model.orientation.name,
                    canvas_ratio = model.canvasRatio.toDouble(),
                )

                model.elements.forEach { element ->
                    dbQuery.insertElement(
                        id = element.id,
                        parent_id = model.id,
                        type = element.serialName,
                        jsonVersion = element.jsonVersion.toLong(),
                        json = element.encodeToString(),
                    )
                }
            }
        }
    }

    suspend fun removeController(id: String) {
        withContext(Dispatchers.IO) {
            dbQuery.removeController(id)
        }
    }

    private fun List<FullControllerDao>.mapControllers(): List<ControllerModel> =
        this.groupBy { it.controllerId }.map { (controllerId, data) ->
            ControllerModel(
                id = controllerId,
                name = data.first().name,
                orientation = ControllerOrientation.from(data.first().orientation),
                canvasRatio = data.first().canvasRatio,
                elements =
                    data.mapNotNull { element ->
                        element.json?.decodeToControllerElementModel()
                    },
            )
        }
}
