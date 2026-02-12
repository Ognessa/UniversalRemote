package com.patorika.universalremote.feature.list.di

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.universalremote.di.ScreenBuilder
import com.patorika.universalremote.feature.editor.main.di.ControllerEditorScreenBuilder
import com.patorika.universalremote.feature.list.model.ControlsListNavigation
import com.patorika.universalremote.feature.list.ui.ControlsListScreen
import org.koin.compose.viewmodel.koinViewModel

class ControlsListScreenBuilder(
    private val editorScreenBuilder: ControllerEditorScreenBuilder,
) : ScreenBuilder {
    override val routeName: String = "ControlsList"

    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.composable(routeName) {
            ControlsListScreen(
                viewModel = koinViewModel(),
                navigate = { handleNavigation(navController, it) },
            )
        }
    }

    private fun handleNavigation(
        navController: NavController,
        type: ControlsListNavigation,
    ) {
        when (type) {
            is ControlsListNavigation.OpenEditor -> {
                navController.navigate(editorScreenBuilder.routeName)
            }
        }
    }
}
