package com.patorika.feature_playground_presentation.model

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel

sealed interface PlaygroundUserEvent {
    object Close : PlaygroundUserEvent

    object OpenDevicePicker : PlaygroundUserEvent

    data class ElementAction(
        val action: String,
    ) : PlaygroundUserEvent

    data class ElementModified(
        val element: ControllerElementModel,
    ) : PlaygroundUserEvent
}
