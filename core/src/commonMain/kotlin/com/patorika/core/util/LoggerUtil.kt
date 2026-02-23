package com.patorika.core.util

expect object LoggerUtil {
    fun e(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    )

    fun d(
        tag: String,
        message: String,
    )

    fun i(
        tag: String,
        message: String,
    )
}
