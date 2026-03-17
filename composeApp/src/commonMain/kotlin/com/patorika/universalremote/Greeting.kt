package com.patorika.universalremote

import com.patorika.core.util.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = "Hello, ${platform.name}!"
}
