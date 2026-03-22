package com.patorika.feature_editor_presentation.api

import com.patorika.feature_controller.main.elements.basic.model.ControllerElementModel
import com.patorika.feature_editor_api.state.EditorSharedState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EditorSharedStateImpl : EditorSharedState {
    private val _newElementsFlow = MutableSharedFlow<List<ControllerElementModel>>()
    override val newElementsFlow = _newElementsFlow.asSharedFlow()

    override suspend fun emitElement(element: ControllerElementModel) {
        _newElementsFlow.emit(listOf(element))
    }

    override suspend fun emitElements(list: List<ControllerElementModel>) {
        if (list.isEmpty()) return
        _newElementsFlow.emit(list)
    }
}
