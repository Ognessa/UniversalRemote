package com.patorika.universalremote.feature.list.di

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.list.ControlsListScreen

class ControlsListScreenBuilder : ScreenBuilder {
    override val routeName: String = "ControlsList"

    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(routeName) {
            ControlsListScreen()
        }
    }
}
