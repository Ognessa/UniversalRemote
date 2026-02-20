package com.patorika.feature_list_presentation.model

sealed class ControlsListNavigation {
    data object OpenEditor : ControlsListNavigation()
}
