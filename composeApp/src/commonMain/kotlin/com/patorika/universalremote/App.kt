package com.patorika.universalremote

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.patorika.core.navigation.ScreenBuilder
import com.patorika.feature_list_api.ControlsListScreenBuilder
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
