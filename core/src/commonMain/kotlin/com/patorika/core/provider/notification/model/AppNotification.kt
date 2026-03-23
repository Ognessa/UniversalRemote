package com.patorika.core.provider.notification.model

import com.patorika.core.provider.text.TextProvider

sealed class AppNotification {
    data class SnackBar(
        val message: TextProvider,
        val actionLabel: TextProvider? = null,
        val onAction: (() -> Unit)? = null,
    ) : AppNotification()

    data class Dialog(
        val title: TextProvider,
        val message: TextProvider,
        val confirmLabel: TextProvider,
        val dismissLabel: TextProvider? = null,
        val onConfirm: () -> Unit,
        val onDismiss: (() -> Unit)? = null,
    ) : AppNotification()
}
