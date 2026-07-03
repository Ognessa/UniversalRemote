package com.patorika.universalremote.components.states

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.patorika.core.navigation.NavDrawerScreenBuilder

@Stable
class AppDrawerState(
    val drawerState: DrawerState,
    val navDrawerScreensList: Map<String, NavDrawerScreenBuilder>,
) {
    var currentDrawerRouteName: String by mutableStateOf("")
}
