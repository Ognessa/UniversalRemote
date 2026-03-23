package com.patorika.universalremote

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.patorika.core.navigation.ScreenBuilder
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.getString
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_list_api.ControlsListScreenBuilder
import org.koin.compose.getKoin
import org.koin.compose.koinInject

@Composable
fun App() {
    // notification setup
    val notificationManager: AppNotificationManager = koinInject()
    val snackbarHostState = remember { SnackbarHostState() }
    var pendingSnackBar by remember { mutableStateOf<AppNotification.SnackBar?>(null) }
    var activeDialog by remember { mutableStateOf<AppNotification.Dialog?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            notificationManager.notifications.collect { notification ->
                when (notification) {
                    is AppNotification.SnackBar -> pendingSnackBar = notification
                    is AppNotification.Dialog -> activeDialog = notification
                }
            }
        }
    }

    pendingSnackBar?.let { snackbar ->
        val message = snackbar.message.getString()
        val actionLabel = snackbar.actionLabel?.getString()

        LaunchedEffect(snackbar) {
            val result =
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                )
            if (result == SnackbarResult.ActionPerformed) {
                snackbar.onAction?.invoke()
            }
            pendingSnackBar = null
        }
    }

    // navigation setup
    val navController = rememberNavController()

    val screensList: List<ScreenBuilder> = getKoin().getAll<ScreenBuilder>()
    val firstScreen: ControlsListScreenBuilder = koinInject()

    // UI setup
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = CoreDimens.current.standardSnackBarInterval),
                )
            },
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = firstScreen.routeName,
            ) {
                screensList.forEach { it.build(this, navController) }
            }

            activeDialog?.let { dialog ->
                AppDialog(
                    dialog = dialog,
                    onDismiss = {
                        dialog.onDismiss?.invoke()
                        activeDialog = null
                    },
                    onConfirm = {
                        dialog.onConfirm()
                        activeDialog = null
                    },
                )
            }
        }
    }
}

@Composable
fun AppDialog(
    dialog: AppNotification.Dialog,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = dialog.title.getString(),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Text(
                text = dialog.message.getString(),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = dialog.confirmLabel.getString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        dismissButton =
            dialog.dismissLabel?.let {
                {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = it.getString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            },
    )
}
