package com.patorika.feature_editor_api.state

import com.patorika.core.controller.model.ControllerModel
import kotlinx.coroutines.flow.SharedFlow

interface EditorSharedState {
    val newElementsFlow: SharedFlow<List<ControllerModel>>

    suspend fun emitElement(element: ControllerModel)

    suspend fun emitElements(list: List<ControllerModel>)
}
