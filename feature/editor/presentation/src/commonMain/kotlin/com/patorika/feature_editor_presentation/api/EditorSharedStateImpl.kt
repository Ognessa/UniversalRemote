package com.patorika.feature_editor_presentation.api

import com.patorika.core.model.controller.ControllerModel
import com.patorika.feature_editor_api.state.EditorSharedState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EditorSharedStateImpl : EditorSharedState {
    private val _newElementsFlow = MutableSharedFlow<List<ControllerModel>>()
    override val newElementsFlow = _newElementsFlow.asSharedFlow()

    override suspend fun emitNewElement(element: ControllerModel) {
        _newElementsFlow.emit(listOf(element))
    }

    override suspend fun emitNewElements(list: List<ControllerModel>) {
        if (list.isEmpty()) return
        _newElementsFlow.emit(list)
    }
}
