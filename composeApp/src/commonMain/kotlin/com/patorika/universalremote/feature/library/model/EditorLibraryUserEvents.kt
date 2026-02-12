package com.patorika.universalremote.feature.library.model

import com.patorika.universalremote.core.model.controller.ControllerType

sealed class EditorLibraryUserEvents {
    data class ElementSelected(
        val element: ControllerType,
    ) : EditorLibraryUserEvents()
}
