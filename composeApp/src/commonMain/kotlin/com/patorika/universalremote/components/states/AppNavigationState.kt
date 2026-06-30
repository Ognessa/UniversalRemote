package com.patorika.universalremote.components.states

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.patorika.core.navigation.NavDrawerScreenBuilder
import com.patorika.core.navigation.openInAppBrowser
import com.patorika.core.provider.navigation.manager.AppNavigationManager
import com.patorika.core.provider.navigation.model.AppNavigationEvent
import org.koin.compose.getKoin

@Stable
class AppDrawerState(
    val drawerState: DrawerState,
    val navDrawerScreensList: Map<String, NavDrawerScreenBuilder>,
) {
    var currentDrawerRouteName: String by mutableStateOf("")
}

@Composable
fun rememberAppNavigationState(navigationManager: AppNavigationManager): AppDrawerState {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navDrawerScreensList = getKoin().getAll<NavDrawerScreenBuilder>().associateBy { it.routeName }
    val state = remember { AppDrawerState(drawerState, navDrawerScreensList) }
    val lifecycleOwner = LocalLifecycleOwner.current

    var browserUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            navigationManager.event.collect { event ->
                when (event) {
                    is AppNavigationEvent.OpenNavDrawer -> {
                        state.currentDrawerRouteName = event.routeName
                        drawerState.open()
                    }

                    is AppNavigationEvent.CloseNavDrawer -> {
                        drawerState.close()
                    }

                    is AppNavigationEvent.OpenBrowser -> {
                        browserUrl = event.url
                    }
                }
            }
        }
    }

    browserUrl?.let { url ->
        openInAppBrowser(url)
        LaunchedEffect(url) { browserUrl = null }
    }

    return state
}
