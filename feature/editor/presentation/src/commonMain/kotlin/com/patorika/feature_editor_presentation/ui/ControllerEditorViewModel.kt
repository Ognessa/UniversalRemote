package com.patorika.feature_editor_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.controller.elements.basic.model.ControllerElementModel
import com.patorika.core.controller.main.model.ControllerModel
import com.patorika.core.util.LoggerUtil
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
        viewModelScope.launch {
            observeNewElements()
        }
    }

    private suspend fun observeNewElements() {
        editorSharedState.newElementsFlow.collectLatest { newElements ->
            _state.update { currentState ->
                currentState.copy(
                    elements =
                        (currentState.elements + newElements)
                            .associateBy { it.id }
                            .values
                            .toList(),
                )
            }
        }
    }

    fun onEvent(event: ControllerEditorUserEvent) {
        viewModelScope.launch {
            when (event) {
                is ControllerEditorUserEvent.ElementClicked -> handleElementClicked(event.id)
                is ControllerEditorUserEvent.ElementModified -> handleElementModified(event.element)
                is ControllerEditorUserEvent.ClearSelection -> clearSelection()
                is ControllerEditorUserEvent.OrientationChanged -> handleOrientationChanges()
                is ControllerEditorUserEvent.Save -> saveController()
            }
        }
    }

    private fun handleElementClicked(id: String) {
        _state.update { it.copy(selectedElementId = id) }
    }

    private fun handleElementModified(element: ControllerElementModel) {
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

    private fun clearSelection() {
        _state.update { it.copy(selectedElementId = null) }
    }

    private fun handleOrientationChanges() {
        _state.update {
            val newOrientation = it.orientation.changeOrientation()

            val newElementsList =
                it.elements.map { element -> element.changeOrientation(newOrientation) }

            it.copy(
                orientation = newOrientation,
                elements = newElementsList,
            )
        }

        LoggerUtil.d(TAG, "Controller orientation changed to ${_state.value.orientation}")
    }

    private fun saveController() {
        val model =
            ControllerModel(
                orientation = _state.value.orientation,
                elements = _state.value.elements,
            )
        // TODO
    }

    companion object {
        private const val TAG = "ControllerEditorViewModel"
    }
}
