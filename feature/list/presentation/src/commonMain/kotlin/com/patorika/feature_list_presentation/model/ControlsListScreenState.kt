package com.patorika.feature_list_presentation.model

import com.patorika.feature_controller.main.model.ControllerModel

data class ControlsListScreenState(
    val isLoading: Boolean = false,
    val controllersList: List<ControllerModel> = emptyList(),
)
