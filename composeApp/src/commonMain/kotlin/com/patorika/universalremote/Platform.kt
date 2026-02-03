package com.patorika.universalremote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
