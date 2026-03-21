package com.patorika.feature_controller.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(
    private val context: Context,
) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(ControllerDatabase.Schema, context, "controllers.db")
}
