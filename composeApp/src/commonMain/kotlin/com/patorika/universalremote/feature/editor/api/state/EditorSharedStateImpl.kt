package com.patorika.universalremote.feature.editor.api.state

import com.patorika.universalremote.core.model.controller.ControllerType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EditorSharedStateImpl : EditorSharedState {
    private val _newElementsFlow = MutableSharedFlow<List<ControllerType>>()
    override val newElementsFlow = _newElementsFlow.asSharedFlow()

    override suspend fun emitNewElement(element: ControllerType) {
        _newElementsFlow.emit(listOf(element))
    }

    override suspend fun emitNewElements(list: List<ControllerType>) {
        if (list.isEmpty()) return
        _newElementsFlow.emit(list)
    }
}
