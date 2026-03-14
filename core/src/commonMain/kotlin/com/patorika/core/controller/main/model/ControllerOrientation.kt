package com.patorika.core.controller.main.model

enum class ControllerOrientation {
    PORTRAIT,
    LANDSCAPE,
    ;

    fun changeOrientation(): ControllerOrientation =
        when (this) {
            PORTRAIT -> LANDSCAPE
            LANDSCAPE -> PORTRAIT
        }
}
