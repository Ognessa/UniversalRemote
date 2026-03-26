package com.patorika.feature_list_presentation.model

import com.patorika.feature_controller.main.model.ControllerModel

sealed interface ControlsListNavigation {
    data class OpenEditor(
        val id: String? = null,
    ) : ControlsListNavigation

    data class OpenTitleEditor(
        val model: ControllerModel,
    ) : ControlsListNavigation
}
