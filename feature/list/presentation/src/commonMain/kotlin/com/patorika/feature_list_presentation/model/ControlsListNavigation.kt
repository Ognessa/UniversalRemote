package com.patorika.feature_list_presentation.model

sealed interface ControlsListNavigation {
    data object OpenEditor : ControlsListNavigation
}
