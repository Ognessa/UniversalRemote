package com.patorika.core.navigation

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
actual fun openInAppBrowser(url: String) {
    val context = LocalContext.current
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(context, url.toUri())
}
