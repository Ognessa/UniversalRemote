package com.patorika.feature_editor_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.controller.model.ControllerModel
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
                _state.update { currentState ->
                    val newList = currentState.elements.toMutableList().apply { addAll(list) }
                    currentState.copy(elements = newList)
                }
            }
        }
    }

    fun onEvent(event: ControllerEditorUserEvent) {
        viewModelScope.launch {
            when (event) {
                is ControllerEditorUserEvent.ElementClicked -> {
                    handleElementClicked(event.id)
                }

                is ControllerEditorUserEvent.ElementModified -> {
                    handleElementModified(event.element)
                }

                is ControllerEditorUserEvent.ClearSelection -> {
                    _state.update { it.copy(selectedElementId = null) }
                }
            }
        }
    }

    private fun handleElementClicked(id: String) {
        _state.update { current ->
            current.copy(selectedElementId = id)
        }
    }

    private fun handleElementModified(element: ControllerModel) {
        _state.update { current ->
            val newList =
                current.elements.toMutableList().map {
                    if (it.id == element.id) element else it
                }

            current.copy(
                elements = newList,
                selectedElementId = element.id,
            )
        }
    }
}
