package com.patorika.core.provider.navigation.manager

import com.patorika.core.provider.navigation.model.AppNavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigationManagerImpl : AppNavigationManager {
    private val _event = MutableSharedFlow<AppNavigationEvent>()
    override val event: SharedFlow<AppNavigationEvent> = _event.asSharedFlow()

    override suspend fun send(event: AppNavigationEvent) {
        _event.emit(event)
    }
}
