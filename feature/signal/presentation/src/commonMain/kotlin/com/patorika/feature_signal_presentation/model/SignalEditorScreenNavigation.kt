package com.patorika.feature_signal_presentation.model

sealed class SignalEditorScreenNavigation {
    data object Close : SignalEditorScreenNavigation()
}
