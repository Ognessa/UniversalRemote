package com.patorika.feature_list_presentation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.patorika.feature_editor_api.navigation.ControllerEditorScreenBuilder
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.feature_list_presentation.model.ControlsListNavigation
import com.patorika.feature_list_presentation.ui.ControlsListScreen
import com.patorika.feature_playground_api.api.PlaygroundScreenBuilder
import com.patorika.feature_title_api.TitleEditorDialogBuilder
import com.patorika.feature_title_api.TitleEditorNavArgs
import com.patorika.feature_title_api.TitleEditorSuccessNavigation
import com.patorika.feature_title_api.toNavArg
import org.koin.compose.viewmodel.koinViewModel

class ControlsListScreenBuilderImpl(
    private val editorScreenBuilder: ControllerEditorScreenBuilder,
    private val titleEditorDialogBuilder: () -> TitleEditorDialogBuilder,
    private val playgroundScreenBuilder: PlaygroundScreenBuilder,
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

            is ControlsListNavigation.OpenTitleEditor -> {
                val args =
                    TitleEditorNavArgs(
                        successNavigation = TitleEditorSuccessNavigation.CLOSE,
                        model = type.model,
                    ).toNavArg()

                navController.navigate("${titleEditorDialogBuilder().routeName}/$args")
            }

            is ControlsListNavigation.OpenPlayground -> {
                navController.navigate("${playgroundScreenBuilder.routeName}/${type.id}")
            }
        }
    }
}
