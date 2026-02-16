package com.patorika.universalremote

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.list.di.ControlsListScreenBuilder
import org.koin.compose.getKoin

@Composable
fun App() {
    val navController = rememberNavController()

    val screensList: List<ScreenBuilder> = getKoin().getAll<ScreenBuilder>()
    val firstScreen: ScreenBuilder = getKoin().get<ControlsListScreenBuilder>()

    MaterialTheme {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = firstScreen.routeName,
        ) {
            screensList.forEach { it.build(this, navController) }
        }
    }
}
