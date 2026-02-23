package com.patorika.feature_signal_presentation.ui

import androidx.lifecycle.ViewModel
import com.patorika.core.controller.model.ControllerModel
import com.patorika.core.util.LoggerUtil

class SignalEditorViewModel(
    val element: ControllerModel,
) : ViewModel() {
    init {
        LoggerUtil.d("DEBUG", "Element: $element")
    }
}
