package com.patorika.core.navigation

import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.UIKit.UIApplication

class IosEmailLauncher : EmailLauncher {
    override fun openEmail(
        recipient: String,
        subject: String?,
        body: String?,
    ): Boolean {
        val urlString =
            buildString {
                append("mailto:")
                append(recipient)
                val params =
                    buildList {
                        subject?.let { add("subject=${it.urlEncode()}") }
                        body?.let { add("body=${it.urlEncode()}") }
                    }
                if (params.isNotEmpty()) {
                    append("?")
                    append(params.joinToString("&"))
                }
            }
        val url = NSURL.URLWithString(urlString) ?: return false
        return if (UIApplication.sharedApplication.canOpenURL(url)) {
            @Suppress("DEPRECATION")
            UIApplication.sharedApplication.openURL(url)
        } else {
            false
        }
    }

    private fun String.urlEncode(): String =
        (this as NSString)
            .stringByAddingPercentEncodingWithAllowedCharacters(
                NSCharacterSet.URLQueryAllowedCharacterSet,
            ) ?: this
}
