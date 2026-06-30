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
import com.patorika.core.navigation.EmailLauncher
import com.patorika.core.navigation.NavDrawerScreenBuilder
import com.patorika.core.navigation.openInAppBrowser
import com.patorika.core.provider.navigation.manager.AppNavigationManager
import com.patorika.core.provider.navigation.model.AppNavigationEvent
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.TextProvider
import org.koin.compose.getKoin
import org.koin.compose.koinInject

@Stable
class AppDrawerState(
    val drawerState: DrawerState,
    val navDrawerScreensList: Map<String, NavDrawerScreenBuilder>,
) {
    var currentDrawerRouteName: String by mutableStateOf("")
}

@Composable
fun rememberAppNavigationState(
    notificationManager: AppNotificationManager,
    navigationManager: AppNavigationManager,
): AppDrawerState {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navDrawerScreensList =
        getKoin().getAll<NavDrawerScreenBuilder>().associateBy { it.routeName }
    val state = remember { AppDrawerState(drawerState, navDrawerScreensList) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val emailLauncher: EmailLauncher = koinInject()

    var browserUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            navigationManager.event.collect { event ->
                when (event) {
                    is AppNavigationEvent.OpenNavDrawer -> {
                        drawerState.close()
                        state.currentDrawerRouteName = event.routeName
                        drawerState.open()
                    }

                    is AppNavigationEvent.CloseNavDrawer -> {
                        drawerState.close()
                    }

                    is AppNavigationEvent.OpenBrowser -> {
                        browserUrl = event.url
                    }

                    is AppNavigationEvent.OpenEmail -> {
                        val sent =
                            emailLauncher.openEmail(event.recipient, event.subject, event.body)
                        if (!sent) {
                            notificationManager.send(
                                AppNotification.SnackBar(
                                    TextProvider.Text("No email app found. Contact: ${event.recipient}"),
                                ),
                            )
                        }
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
