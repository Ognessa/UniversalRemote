package com.patorika.feature_title_presentation.api

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.savedstate.read
import com.patorika.feature_list_api.ControlsListScreenBuilder
import com.patorika.feature_title_api.TitleEditorDialogBuilder
import com.patorika.feature_title_api.TitleEditorNavArgs
import com.patorika.feature_title_api.titleEditorNavArgsFromNavArg
import com.patorika.feature_title_presentation.model.TitleEditorNavigation
import com.patorika.feature_title_presentation.ui.TitleEditorDialog
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class TitleEditorDialogBuilderImpl(
    private val controllerListScreenBuilder: () -> ControlsListScreenBuilder,
) : TitleEditorDialogBuilder {
    override fun build(
        builder: NavGraphBuilder,
        navController: NavController,
    ) {
        builder.dialog(
            route = "$routeName/{$TITLE_EDITOR_ARGS}",
        ) { backStackEntry ->
            val args = backStackEntry.extractTitleEditorNavArgs()

            TitleEditorDialog(
                viewModel = koinViewModel { parametersOf(args) },
                navigate = { handleNavigation(it, navController) },
            )
        }
    }

    private fun NavBackStackEntry.extractTitleEditorNavArgs(): TitleEditorNavArgs {
        val encoded =
            this.arguments?.read {
                getStringOrNull(TITLE_EDITOR_ARGS)
            } ?: error("Missing nav arg: $TITLE_EDITOR_ARGS")

        return titleEditorNavArgsFromNavArg(encoded)
    }

    private fun handleNavigation(
        type: TitleEditorNavigation,
        navController: NavController,
    ) {
        when (type) {
            is TitleEditorNavigation.Close -> {
                navController.popBackStack()
            }

            is TitleEditorNavigation.OpenList -> {
                navController.popBackStack(
                    route = controllerListScreenBuilder().routeName,
                    inclusive = false,
                )
            }
        }
    }

    companion object {
        const val TITLE_EDITOR_ARGS = "TITLE_EDITOR_ARGS"
    }
}
