package com.patorika.core.provider.notification.manager

import com.patorika.core.provider.notification.model.AppNotification
import kotlinx.coroutines.flow.SharedFlow

interface AppNotificationManager {
    val notifications: SharedFlow<AppNotification>

    suspend fun send(notification: AppNotification)
}
