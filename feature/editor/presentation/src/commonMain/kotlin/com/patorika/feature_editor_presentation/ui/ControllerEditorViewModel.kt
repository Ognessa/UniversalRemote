package com.patorika.feature_editor_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.model.controller.ControllerType
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_editor_presentation.model.ControllerEditorScreenState
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ControllerEditorViewModel(
    private val editorSharedState: EditorSharedState,
) : ViewModel() {
    private val _state = MutableStateFlow(ControllerEditorScreenState())
    val state = _state.asStateFlow()

    init {
        observeNewElements()
    }

    private fun observeNewElements() {
        viewModelScope.launch {
            editorSharedState.newElementsFlow.collectLatest { list ->
                _state.update {
                    val newList = it.elements.toMutableList().apply { addAll(list) }
                    it.copy(elements = newList)
                }
            }
        }
    }

    fun onEvent(event: ControllerEditorUserEvent) {
        viewModelScope.launch {
            when (event) {
                is ControllerEditorUserEvent.ElementClicked -> {
                    handleElementClicked(event.index)
                }

                is ControllerEditorUserEvent.ElementModified -> {
                    handleElementModified(event.index, event.element)
                }

                is ControllerEditorUserEvent.ClearSelection -> {
                    _state.update { it.copy(selectedElementIndex = null) }
                }
            }
        }
    }

    private fun handleElementClicked(index: Int) {
        _state.update { current ->
            current.copy(selectedElementIndex = index)
        }
    }

    private fun handleElementModified(
        index: Int,
        element: ControllerType,
    ) {
        _state.update { current ->
            val newList = current.elements.toMutableList().apply { this[index] = element }
            current.copy(
                elements = newList,
                selectedElementIndex = index,
            )
        }
    }
}
