package com.patorika.feature_controller.main.model

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
