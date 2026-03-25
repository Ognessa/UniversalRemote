package com.patorika.feature_library_presentation.model

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel

sealed interface EditorLibraryUserEvents {
    data class ElementSelected(
        val element: ControllerElementModel,
    ) : EditorLibraryUserEvents
}
