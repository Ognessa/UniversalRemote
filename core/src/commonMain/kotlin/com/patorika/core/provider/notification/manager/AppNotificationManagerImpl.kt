package com.patorika.core.provider.notification.manager

import com.patorika.core.provider.notification.model.AppNotification
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNotificationManagerImpl : AppNotificationManager {
    private val _notifications = MutableSharedFlow<AppNotification>()
    override val notifications = _notifications.asSharedFlow()

    override suspend fun send(notification: AppNotification) {
        _notifications.emit(notification)
    }
}
