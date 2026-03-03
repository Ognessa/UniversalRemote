package com.patorika.feature_signal_api

import com.patorika.core.navigation.ScreenBuilder

interface SignalEditorScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "SignalEditor"
}
