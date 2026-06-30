package com.patorika.universalremote.components.states

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.getString
import com.patorika.universalremote.components.dialog.AppDialog

@Stable
class AppNotificationState(
    val snackbarHostState: SnackbarHostState,
)

@Composable
fun rememberAppNotificationState(notificationManager: AppNotificationManager): AppNotificationState {
    val snackbarHostState = remember { SnackbarHostState() }
    val state = remember { AppNotificationState(snackbarHostState) }
    val lifecycleOwner = LocalLifecycleOwner.current

    var pendingSnackBar by remember { mutableStateOf<AppNotification.SnackBar?>(null) }
    var activeDialog by remember { mutableStateOf<AppNotification.Dialog?>(null) }

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
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
            )
            if (result == SnackbarResult.ActionPerformed) {
                snackbar.onAction?.invoke()
            }
            pendingSnackBar = null
        }
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

    return state
}