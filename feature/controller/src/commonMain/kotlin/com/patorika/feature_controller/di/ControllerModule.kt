package com.patorika.feature_controller.di

import com.patorika.feature_controller.data.database.DatabaseDriverFactory
import com.patorika.feature_controller.data.repository.ControllerRepositoryImpl
import com.patorika.feature_controller.domain.repository.ControllerRepository
import org.koin.dsl.module

val controllerModule =
    module {
        single<ControllerRepository> {
            ControllerRepositoryImpl(
                databaseDriverFactory = get<DatabaseDriverFactory>(),
            )
        }
    }
