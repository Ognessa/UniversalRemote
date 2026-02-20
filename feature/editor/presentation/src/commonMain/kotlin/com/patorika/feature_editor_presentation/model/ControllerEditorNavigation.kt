package com.patorika.feature_editor_presentation.model

sealed class ControllerEditorNavigation {
    data object OpenLibrary : ControllerEditorNavigation()
}
