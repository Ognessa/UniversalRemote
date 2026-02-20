package com.patorika.feature_list_presentation.model

data class ControlsListScreenState(
    val isLoading: Boolean = false,
    val controllersList: List<String> = emptyList(),
)
