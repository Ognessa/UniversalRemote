package com.patorika.universalremote.feature.list

sealed class ControlsListEvents {
    data object CreateNewControl : ControlsListEvents()
}
