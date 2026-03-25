package com.patorika.feature_library_presentation.model

sealed interface EditorLibraryNavigation {
    data object CloseLibrary : EditorLibraryNavigation
}
