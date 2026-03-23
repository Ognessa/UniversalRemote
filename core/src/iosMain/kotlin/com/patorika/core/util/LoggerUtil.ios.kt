package com.patorika.core.util

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

actual object LoggerUtil {
    @OptIn(ExperimentalNativeApi::class)
    private val loggerAllowed = Platform.isDebugBinary

    actual fun e(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        if (loggerAllowed) {
            if (throwable != null) {
                NSLog("ERROR: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
            } else {
                NSLog("ERROR: [$tag] $message")
            }
        }
    }

    actual fun d(
        tag: String,
        message: String,
    ) {
        if (loggerAllowed) {
            NSLog("DEBUG: [$tag] $message")
        }
    }

    actual fun i(
        tag: String,
        message: String,
    ) {
        if (loggerAllowed) {
            NSLog("INFO: [$tag] $message")
        }
    }
}
