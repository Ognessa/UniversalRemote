package com.patorika.universalremote.feature.list.model

sealed class ControlsListEvents {
    data object Refresh : ControlsListEvents()

    data object CreateNewControl : ControlsListEvents()
}
