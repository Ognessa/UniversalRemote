package com.patorika.core.provider.navigation.model

sealed class AppNavigationEvent {
    data class OpenNavDrawer(
        val routeName: String,
    ) : AppNavigationEvent()

    data object CloseNavDrawer : AppNavigationEvent()

    data class OpenBrowser(
        val url: String,
    ) : AppNavigationEvent()

    data class OpenEmail(
        val recipient: String,
        val subject: String? = null,
        val body: String? = null,
    ) : AppNavigationEvent()
}
