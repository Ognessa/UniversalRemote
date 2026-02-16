package com.patorika.universalremote.feature.list.model

sealed class ControlsListNavigation {
    data object OpenEditor : ControlsListNavigation()
}
