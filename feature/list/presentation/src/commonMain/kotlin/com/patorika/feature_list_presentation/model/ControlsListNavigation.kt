package com.patorika.feature_list_presentation.model

import com.patorika.feature_controller.presentation.model.ControllerModel

sealed interface ControlsListNavigation {
    data class OpenEditor(
        val id: String? = null,
    ) : ControlsListNavigation

    data class OpenTitleEditor(
        val model: ControllerModel,
    ) : ControlsListNavigation

    data class OpenPlayground(
        val id: String,
    ) : ControlsListNavigation

    data object OpenGeneralMenu : ControlsListNavigation
}
