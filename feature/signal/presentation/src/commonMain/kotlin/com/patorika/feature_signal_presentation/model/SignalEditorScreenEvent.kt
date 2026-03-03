package com.patorika.feature_signal_presentation.model

import com.patorika.core.controller.model.ControllerModel

sealed class SignalEditorScreenEvent {
    data class OnElementModified(
        val element: ControllerModel,
    ) : SignalEditorScreenEvent()

    data object SaveChanges : SignalEditorScreenEvent()
}
