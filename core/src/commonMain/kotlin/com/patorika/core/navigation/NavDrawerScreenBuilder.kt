package com.patorika.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

interface NavDrawerScreenBuilder {
    val routeName: String

    @Composable
    fun Content(navController: NavController) {
    }
}
