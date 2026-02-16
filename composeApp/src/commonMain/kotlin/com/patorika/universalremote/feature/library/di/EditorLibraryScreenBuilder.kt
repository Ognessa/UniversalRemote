package com.patorika.universalremote.feature.library.di

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.universalremote.core.ui.animation.dialogSlideIntoContainer
import com.patorika.universalremote.core.ui.animation.dialogSlideOutOfContainer
import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.library.model.EditorLibraryNavigation
import com.patorika.universalremote.feature.library.ui.EditorLibraryScreen
import org.koin.compose.viewmodel.koinViewModel

class EditorLibraryScreenBuilder : ScreenBuilder {
    override val routeName: String = "EditorLibrary"

    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(
            route = routeName,
            enterTransition = { dialogSlideIntoContainer() },
            exitTransition = { dialogSlideOutOfContainer() },
        ) {
            EditorLibraryScreen(
                viewModel = koinViewModel(),
                navigate = { handleNavigation(navController, it) },
            )
        }
    }

    private fun handleNavigation(
        navController: NavController,
        type: EditorLibraryNavigation,
    ) {
        when (type) {
            is EditorLibraryNavigation.CloseLibrary -> navController.popBackStack()
        }
    }
}
