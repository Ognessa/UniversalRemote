package com.patorika.core.navigation

interface EmailLauncher {
    fun openEmail(
        recipient: String,
        subject: String? = null,
        body: String? = null,
    ): Boolean
}
