package com.patorika.core.di

import com.patorika.core.provider.navigation.manager.AppNavigationManager
import com.patorika.core.provider.navigation.manager.AppNavigationManagerImpl
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.manager.AppNotificationManagerImpl
import org.koin.dsl.module

val coreModule =
    module {
        single<AppNotificationManager> { AppNotificationManagerImpl() }
        single<AppNavigationManager> { AppNavigationManagerImpl() }
    }
