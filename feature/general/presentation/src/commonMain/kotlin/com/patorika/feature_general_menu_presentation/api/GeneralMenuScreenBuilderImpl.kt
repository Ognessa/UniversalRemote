package com.patorika.feature_general_menu_presentation.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.patorika.core.AppConstants
import com.patorika.core.provider.navigation.manager.AppNavigationManager
import com.patorika.core.provider.navigation.model.AppNavigationEvent
import com.patorika.feature_general_menu_api.GeneralMenuScreenBuilder
import com.patorika.feature_general_menu_presentation.model.GeneralMenuNavigation
import com.patorika.feature_general_menu_presentation.ui.GeneralMenuScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GeneralMenuScreenBuilderImpl(
    private val appNavigationManager: AppNavigationManager,
) : GeneralMenuScreenBuilder {
    @Composable
    override fun Content(navController: NavController) {
        GeneralMenuScreen(
            navigate = { scope, type -> handleNavigation(scope, navController, type) },
        )
    }

    private fun handleNavigation(
        scope: CoroutineScope,
        navController: NavController,
        type: GeneralMenuNavigation,
    ) {
        when (type) {
            is GeneralMenuNavigation.OpenInstruction -> {
                scope.launch {
                    appNavigationManager.send(AppNavigationEvent.OpenBrowser(AppConstants.INSTRUCTION_URL))
                }
            }

            is GeneralMenuNavigation.Support -> {
                scope.launch {
                    appNavigationManager.send(AppNavigationEvent.OpenEmail(AppConstants.SUPPORT_EMAIL))
                }
            }

            is GeneralMenuNavigation.OpenPrivacyPolicy -> {
                scope.launch {
                    appNavigationManager.send(AppNavigationEvent.OpenBrowser(AppConstants.PRIVACY_POLICY))
                }
            }
        }
    }
}
