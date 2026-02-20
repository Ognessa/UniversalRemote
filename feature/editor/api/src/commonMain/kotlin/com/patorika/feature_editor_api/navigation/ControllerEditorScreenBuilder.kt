package com.patorika.feature_editor_api.navigation

import com.patorika.core.navigation.ScreenBuilder

interface ControllerEditorScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "ControllerEditor"
}
