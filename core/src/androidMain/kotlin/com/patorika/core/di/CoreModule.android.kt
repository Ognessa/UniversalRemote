package com.patorika.core.di

import com.patorika.core.navigation.AndroidEmailLauncher
import com.patorika.core.navigation.EmailLauncher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidCoreModule =
    module {
        single<EmailLauncher> { AndroidEmailLauncher(context = androidContext()) }
    }