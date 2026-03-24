package com.patorika.feature_list_presentation.model

sealed interface ControlsListEvents {
    data object Refresh : ControlsListEvents
}
