package com.patorika.feature_editor_api.state

import com.patorika.core.model.controller.ControllerType
import kotlinx.coroutines.flow.SharedFlow

interface EditorSharedState {
    val newElementsFlow: SharedFlow<List<ControllerType>>

    suspend fun emitNewElement(element: ControllerType)

    suspend fun emitNewElements(list: List<ControllerType>)
}
