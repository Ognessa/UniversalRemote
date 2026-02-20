package com.patorika.feature_library_presentation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.core.ui.animation.dialogSlideIntoContainer
import com.patorika.core.ui.animation.dialogSlideOutOfContainer
import com.patorika.feature_library_api.EditorLibraryScreenBuilder
import com.patorika.feature_library_presentation.model.EditorLibraryNavigation
import com.patorika.feature_library_presentation.ui.EditorLibraryScreen
import org.koin.compose.viewmodel.koinViewModel

class EditorLibraryScreenBuilderImpl : EditorLibraryScreenBuilder {
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
