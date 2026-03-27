package com.patorika.feature_title_api

import com.patorika.core.navigation.ScreenBuilder

interface TitleEditorDialogBuilder : ScreenBuilder {
    override val routeName: String
        get() = "TitleEditorDialog"
}
