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

    companion object {
        fun from(name: String): ControllerOrientation =
            entries.firstOrNull {
                it.name.equals(name, ignoreCase = true)
            } ?: PORTRAIT
    }
}
