package com.patorika.core.util

enum class Platform {
    IOS,
    ANDROID,
}

expect fun getPlatform(): Platform
