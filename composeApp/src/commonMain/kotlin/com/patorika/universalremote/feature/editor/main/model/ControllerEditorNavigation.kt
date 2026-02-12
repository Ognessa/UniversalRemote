package com.patorika.universalremote.feature.editor.main.model

sealed class ControllerEditorNavigation {
    data object OpenLibrary : ControllerEditorNavigation()
}
