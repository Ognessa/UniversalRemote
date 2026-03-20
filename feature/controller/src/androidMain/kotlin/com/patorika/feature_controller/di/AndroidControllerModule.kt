package com.patorika.feature_controller.di

import com.patorika.feature_controller.data.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidControllerModule =
    module {
        factory { DatabaseDriverFactory(context = androidContext()) }
    }
