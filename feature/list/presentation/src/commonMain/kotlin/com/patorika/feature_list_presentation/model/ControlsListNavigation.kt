package com.patorika.feature_list_presentation.model

sealed interface ControlsListNavigation {
    data class OpenEditor(
        val id: String? = null,
    ) : ControlsListNavigation
}
