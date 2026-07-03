package com.patorika.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.UIKitViewController
import platform.Foundation.NSURL
import platform.SafariServices.SFSafariViewController

@Composable
actual fun openInAppBrowser(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    val safariViewController = SFSafariViewController(uRL = nsUrl)

    UIKitViewController(
        factory = { safariViewController },
        update = { },
    )
}
