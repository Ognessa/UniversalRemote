package com.patorika.feature_library_presentation.model

import com.patorika.core.model.controller.ControllerType

sealed class EditorLibraryUserEvents {
    data class ElementSelected(
        val element: ControllerType,
    ) : EditorLibraryUserEvents()
}
