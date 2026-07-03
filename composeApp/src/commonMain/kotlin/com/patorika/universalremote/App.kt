package com.patorika.universalremote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.patorika.core.navigation.ScreenBuilder
import com.patorika.core.provider.navigation.manager.AppNavigationManager
import com.patorika.core.provider.navigation.model.AppNavigationEvent
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.universalremote.components.states.rememberAppNavigationState
import com.patorika.universalremote.components.states.rememberAppNotificationState
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import org.koin.compose.koinInject

@Composable
fun App() {
    // Notifications
    val notificationManager: AppNotificationManager = koinInject()
    val notificationState = rememberAppNotificationState(notificationManager)

    // Internal app's navigation
    val navigationManager: AppNavigationManager = koinInject()
    val appNavigationState = rememberAppNavigationState(notificationManager, navigationManager)

    // NavHost navigation
    val navController = rememberNavController()
    val screensList: List<ScreenBuilder> = getKoin().getAll<ScreenBuilder>()
    val firstScreen: ControlsListScreenBuilder = koinInject()

    // Backstack handler
    val scope = LocalLifecycleOwner.current.lifecycleScope
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = appNavigationState.drawerState.isOpen,
        onBackCompleted = {
            scope.launch {
                navigationManager.send(AppNavigationEvent.CloseNavDrawer)
            }
        },
    )

    // Screen content
    MaterialTheme {
        ModalNavigationDrawer(
            drawerState = appNavigationState.drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                    appNavigationState.navDrawerScreensList[appNavigationState.currentDrawerRouteName]?.Content(
                        navController,
                    )
                }
            },
        ) {
            Scaffold(modifier = Modifier.fillMaxSize()) { screenPaddings ->
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = firstScreen.routeName,
                ) {
                    screensList.forEach { it.build(this, navController) }
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(screenPaddings)
                            .padding(top = TopAppBarDefaults.TopAppBarExpandedHeight),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    SnackbarHost(hostState = notificationState.snackbarHostState)
                }
            }
        }
    }
}
