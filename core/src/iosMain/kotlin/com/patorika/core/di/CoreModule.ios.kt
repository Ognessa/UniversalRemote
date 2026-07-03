package com.patorika.core.di

import com.patorika.core.navigation.EmailLauncher
import com.patorika.core.navigation.IosEmailLauncher
import org.koin.dsl.module

val iosCoreModule =
    module {
        single<EmailLauncher> { IosEmailLauncher() }
    }
