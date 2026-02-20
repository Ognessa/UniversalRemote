package com.patorika.feature_library_presentation.model

sealed class EditorLibraryNavigation {
    data object CloseLibrary : EditorLibraryNavigation()
}
