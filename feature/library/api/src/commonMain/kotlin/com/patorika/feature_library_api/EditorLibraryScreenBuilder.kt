package com.patorika.feature_library_api

import com.patorika.core.navigation.ScreenBuilder

interface EditorLibraryScreenBuilder : ScreenBuilder {
    override val routeName: String
        get() = "EditorLibrary"
}
