package com.patorika.feature_signal_presentation.model

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel

sealed interface SignalEditorScreenEvent {
    data class OnElementModified(
        val element: ControllerElementModel,
    ) : SignalEditorScreenEvent

    data object SaveChanges : SignalEditorScreenEvent
}
