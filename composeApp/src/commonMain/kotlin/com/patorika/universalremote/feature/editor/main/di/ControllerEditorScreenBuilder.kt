package com.patorika.universalremote.feature.editor.main.di

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.editor.main.model.ControllerEditorNavigation
import com.patorika.universalremote.feature.editor.main.ui.ControlersEditorScreen
import com.patorika.universalremote.feature.library.di.EditorLibraryScreenBuilder
import org.koin.compose.viewmodel.koinViewModel

class ControllerEditorScreenBuilder(
    private val editorLibraryScreenBuilder: EditorLibraryScreenBuilder,
) : ScreenBuilder {
    override val routeName: String = "ControllerEditor"

    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(routeName) {
            ControlersEditorScreen(
                viewModel = koinViewModel(),
                navigate = { handleNavigation(navController, it) },
            )
        }
    }

    private fun handleNavigation(
        navController: NavController,
        type: ControllerEditorNavigation,
    ) {
        when (type) {
            is ControllerEditorNavigation.OpenLibrary -> {
                navController.navigate(editorLibraryScreenBuilder.routeName)
            }
        }
    }
}
