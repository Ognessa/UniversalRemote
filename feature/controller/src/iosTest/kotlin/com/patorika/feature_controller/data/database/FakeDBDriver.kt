package com.patorika.feature_controller.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver

actual fun createTestDatabaseDriver(): SqlDriver = inMemoryDriver(ControllerDatabase.Schema)
