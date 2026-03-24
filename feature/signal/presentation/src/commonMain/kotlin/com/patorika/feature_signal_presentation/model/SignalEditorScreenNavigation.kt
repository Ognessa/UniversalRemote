package com.patorika.feature_signal_presentation.model

sealed interface SignalEditorScreenNavigation {
    data object Close : SignalEditorScreenNavigation
}
