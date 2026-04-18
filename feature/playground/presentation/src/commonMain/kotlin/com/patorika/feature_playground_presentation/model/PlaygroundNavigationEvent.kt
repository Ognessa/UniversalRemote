package com.patorika.feature_playground_presentation.model

sealed interface PlaygroundNavigationEvent {
    object Close : PlaygroundNavigationEvent

    object OpenDevicePicker : PlaygroundNavigationEvent
}
