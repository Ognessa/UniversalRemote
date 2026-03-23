package com.patorika.core.di

import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.manager.AppNotificationManagerImpl
import org.koin.dsl.module

val coreModule =
    module {
        single<AppNotificationManager> { AppNotificationManagerImpl() }
    }
