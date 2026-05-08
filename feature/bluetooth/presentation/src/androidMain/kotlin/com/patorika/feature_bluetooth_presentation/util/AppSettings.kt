package com.patorika.feature_bluetooth_presentation.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberOpenAppSettings(): () -> Unit {
    val context = LocalContext.current
    return {
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        context.startActivity(intent)
    }
}
