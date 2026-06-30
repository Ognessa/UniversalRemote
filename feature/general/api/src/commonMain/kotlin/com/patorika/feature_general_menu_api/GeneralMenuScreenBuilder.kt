package com.patorika.feature_general_menu_api

import com.patorika.core.navigation.NavDrawerScreenBuilder

interface GeneralMenuScreenBuilder : NavDrawerScreenBuilder {
    override val routeName: String
        get() = "GeneralMenu"
}
