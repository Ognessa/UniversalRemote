package com.patorika.feature_library_presentation.model

import com.patorika.feature_controller.elements.basic.model.ControllerElementModel

sealed class EditorLibraryUserEvents {
    data class ElementSelected(
        val element: ControllerElementModel,
    ) : EditorLibraryUserEvents()
}
