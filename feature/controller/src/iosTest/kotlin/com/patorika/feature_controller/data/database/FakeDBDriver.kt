package com.patorika.feature_controller.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual fun createTestDatabaseDriver(): SqlDriver =
    NativeSqliteDriver(
        ControllerDatabase.Schema,
        ":memory:",
    )
