package com.patorika.feature_controller.di

import com.patorika.feature_controller.data.database.DatabaseDriverFactory
import org.koin.dsl.module

val iosControllerModule =
    module {
        factory { DatabaseDriverFactory() }
    }
