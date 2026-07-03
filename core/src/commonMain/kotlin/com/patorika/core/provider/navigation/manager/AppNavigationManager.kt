package com.patorika.core.provider.navigation.manager

import com.patorika.core.provider.navigation.model.AppNavigationEvent
import kotlinx.coroutines.flow.SharedFlow

interface AppNavigationManager {
    val event: SharedFlow<AppNavigationEvent>

    suspend fun send(event: AppNavigationEvent)
}
