package com.patorika.universalremote.di

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface ScreenBuilder {
    val routeName: String

    fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    )
}
