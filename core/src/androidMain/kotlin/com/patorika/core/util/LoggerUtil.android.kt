package com.patorika.core.util

import android.util.Log
import com.patorika.core.BuildConfig

actual object LoggerUtil {
    private val loggerAllowed = BuildConfig.DEBUG

    actual fun e(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        if (loggerAllowed) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    actual fun d(
        tag: String,
        message: String,
    ) {
        if (loggerAllowed) {
            Log.d(tag, message)
        }
    }

    actual fun i(
        tag: String,
        message: String,
    ) {
        if (loggerAllowed) {
            Log.i(tag, message)
        }
    }
}
