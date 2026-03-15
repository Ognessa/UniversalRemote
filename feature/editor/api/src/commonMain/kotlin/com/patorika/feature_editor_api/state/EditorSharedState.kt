package com.patorika.feature_editor_api.state

import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import kotlinx.coroutines.flow.SharedFlow

interface EditorSharedState {
    val newElementsFlow: SharedFlow<List<ControllerElementModel>>

    suspend fun emitElement(element: ControllerElementModel)

    suspend fun emitElements(list: List<ControllerElementModel>)
}
