package com.patorika.feature_list_presentation.model

sealed class ControlsListEvents {
    data object Refresh : ControlsListEvents()

    data object CreateNewControl : ControlsListEvents()
}
