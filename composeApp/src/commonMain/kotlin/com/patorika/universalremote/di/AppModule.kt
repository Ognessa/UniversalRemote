package com.patorika.universalremote.di

import com.patorika.universalremote.feature.list.di.controlsListModule
import org.koin.dsl.module

val appModule =
    module {
        includes(controlsListModule)
    }
