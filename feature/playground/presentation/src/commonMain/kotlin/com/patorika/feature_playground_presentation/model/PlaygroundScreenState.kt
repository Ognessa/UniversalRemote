package com.patorika.feature_playground_presentation.model

import com.patorika.feature_controller.presentation.model.ControllerModel

data class PlaygroundScreenState(
    val isLoading: Boolean = false,
    val controller: ControllerModel? = null,
)
