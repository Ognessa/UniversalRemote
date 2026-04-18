package com.patorika.feature_playground_api.api

import com.patorika.core.navigation.ScreenBuilder

interface PlaygroundScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "Playground"
}
