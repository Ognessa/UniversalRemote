package com.patorika.feature_controller.data.repository

import app.cash.sqldelight.db.SqlDriver
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.data.database.Database
import com.patorika.feature_controller.domain.repository.ControllerRepository
import com.patorika.feature_controller.presentation.model.ControllerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ControllerRepositoryImpl(
    driver: SqlDriver,
) : ControllerRepository {
    private val database = Database(driver)

    override fun getAllControllers(): Flow<Result<List<ControllerModel>>> =
        database
            .getAllControllers()
            .map { Result.success(it) }
            .catch { error ->
                LoggerUtil.d(TAG, "Failed to get all controllers\n$error")
                emit(Result.failure(error))
            }

    override suspend fun getControllerById(id: String): Result<ControllerModel> =
        runCatching {
            withContext(Dispatchers.IO) {
                database.getControllerById(id)?.let { result ->
                    Result.success(result)
                } ?: Result.failure(NullPointerException("Failed to get a controller by id $id"))
            }
        }.getOrElse { error ->
            LoggerUtil.d(TAG, "Failed to get a controller by id $id\n$error")
            Result.failure(error)
        }

    override suspend fun insertController(model: ControllerModel): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                database.insertController(model)
                Result.success(Unit)
            }
        }.getOrElse { error ->
            LoggerUtil.d(
                TAG,
                "Failed to inserd controller\n" +
                    "Model: $model\n" +
                    "Error: $error",
            )
            Result.failure(error)
        }

    override suspend fun removeController(id: String): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                database.removeController(id)
                Result.success(Unit)
            }
        }.getOrElse { error ->
            LoggerUtil.d(TAG, "Failed to remove controller by id $id\n$error")
            Result.failure(error)
        }

    companion object {
        private const val TAG = "ControllerRepositoryImpl"
    }
}
