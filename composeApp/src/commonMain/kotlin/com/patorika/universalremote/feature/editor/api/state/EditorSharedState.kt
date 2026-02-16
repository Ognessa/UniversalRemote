package com.patorika.universalremote.feature.editor.api.state

import com.patorika.universalremote.core.model.controller.ControllerType
import kotlinx.coroutines.flow.SharedFlow

interface EditorSharedState {
    val newElementsFlow: SharedFlow<List<ControllerType>>

    suspend fun emitNewElement(element: ControllerType)

    suspend fun emitNewElements(list: List<ControllerType>)
}
