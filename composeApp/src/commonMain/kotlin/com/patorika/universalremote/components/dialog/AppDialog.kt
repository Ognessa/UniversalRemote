package com.patorika.universalremote.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.getString

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
