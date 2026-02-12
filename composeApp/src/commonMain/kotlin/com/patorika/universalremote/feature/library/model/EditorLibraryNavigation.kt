package com.patorika.universalremote.feature.library.model

sealed class EditorLibraryNavigation {
    data object CloseLibrary : EditorLibraryNavigation()
}
