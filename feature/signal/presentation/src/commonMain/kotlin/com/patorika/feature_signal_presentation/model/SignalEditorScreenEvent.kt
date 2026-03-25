package com.patorika.feature_signal_presentation.model

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel

sealed interface SignalEditorScreenEvent {
    data class OnElementModified(
        val element: ControllerElementModel,
    ) : SignalEditorScreenEvent

    data object SaveChanges : SignalEditorScreenEvent
}
