package com.patorika.feature_list_presentation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.feature_list_presentation.model.ControlsListNavigation
import com.patorika.feature_list_presentation.ui.ControlsListScreen
import org.koin.compose.viewmodel.koinViewModel

class ControlsListScreenBuilderImpl(
    private val editorScreenBuilder: ControllerEditorScreenBuilder,
) : ControlsListScreenBuilder {
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
                navController.navigate("${editorScreenBuilder.routeName}/${type.id}")
            }
        }
    }
}
