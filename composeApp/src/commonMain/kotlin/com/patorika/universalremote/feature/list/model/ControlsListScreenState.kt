package com.patorika.universalremote.feature.list.model

data class ControlsListScreenState(
    val isLoading: Boolean = false,
    val controllersList: List<String> = emptyList(),
)
