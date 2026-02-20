package com.patorika.feature_list_api

import com.patorika.core.navigation.ScreenBuilder

interface ControlsListScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "ControlsList"
}
