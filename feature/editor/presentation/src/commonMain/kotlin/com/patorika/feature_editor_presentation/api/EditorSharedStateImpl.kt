package com.patorika.feature_editor_presentation.api

import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_editor_api.state.EditorSharedState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EditorSharedStateImpl : EditorSharedState {
    private val _elementsFlow = MutableSharedFlow<List<ControllerElementModel>>()
    override val elementsFlow = _elementsFlow.asSharedFlow()

    override suspend fun emitElement(element: ControllerElementModel) {
        _elementsFlow.emit(listOf(element))
    }

    override suspend fun emitElements(list: List<ControllerElementModel>) {
        if (list.isEmpty()) return
        _elementsFlow.emit(list)
    }
}
