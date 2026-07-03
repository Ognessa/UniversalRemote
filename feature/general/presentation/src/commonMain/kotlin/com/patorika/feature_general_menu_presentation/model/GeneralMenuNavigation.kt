package com.patorika.feature_general_menu_presentation.model

sealed class GeneralMenuNavigation {
    data object OpenInstruction : GeneralMenuNavigation()

    data object Support : GeneralMenuNavigation()

    data object OpenPrivacyPolicy : GeneralMenuNavigation()
}
