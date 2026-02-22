package com.patorika.feature_library_presentation.model

import com.patorika.core.model.controller.ControllerModel

sealed class EditorLibraryUserEvents {
    data class ElementSelected(
        val element: ControllerModel,
    ) : EditorLibraryUserEvents()
}
