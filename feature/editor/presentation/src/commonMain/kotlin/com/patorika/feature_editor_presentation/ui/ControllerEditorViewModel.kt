package com.patorika.feature_editor_presentation.ui

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.main.model.ControllerModel
import com.patorika.feature_controller.main.model.ControllerOrientation
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.model.ControllerEditorScreenState
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import com.patorika.feature_editor_presentation.usecase.SaveControllerUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ControllerEditorViewModel(
    private val editorSharedState: EditorSharedState,
    private val saveControllerUseCase: SaveControllerUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ControllerEditorScreenState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ControllerEditorNavigation>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            observeNewElements()
        }
    }

    private suspend fun observeNewElements() {
        editorSharedState.newElementsFlow.collectLatest { newElements ->
            _state.update { currentState ->
                val normalizedElements =
                    newElements.map { element ->
                        if (currentState.elements.firstOrNull { it.id == element.id } == null) {
                            element.setInitialSizeScale().let {
                                if (currentState.orientation == ControllerOrientation.LANDSCAPE) {
                                    it.changeOrientation()
                                } else {
                                    it
                                }
                            }
                        } else {
                            element
                        }
                    }

                currentState.copy(
                    elements =
                        (currentState.elements + normalizedElements)
                            .associateBy { it.id }
                            .values
                            .toList(),
                )
            }
        }
    }

    private fun ControllerElementModel.setInitialSizeScale(): ControllerElementModel {
        val canvasSizeDp = _state.value.canvasSizeDp
        return this.changeDisplayParameters(
            this.displayParameters.copy(
                scaleSize =
                    Size(
                        width = this.getDefaultSize().width / canvasSizeDp.width,
                        height = this.getDefaultSize().height / canvasSizeDp.height,
                    ),
            ),
        )
    }

    fun onEvent(event: ControllerEditorUserEvent) {
        viewModelScope.launch {
            when (event) {
                is ControllerEditorUserEvent.ElementClicked -> onElementClicked(event.id)
                is ControllerEditorUserEvent.ElementModified -> onElementModified(event.element)
                is ControllerEditorUserEvent.ClearSelection -> onClearSelection()
                is ControllerEditorUserEvent.OrientationChanged -> onOrientationChanges()
                is ControllerEditorUserEvent.Save -> onSaveController()
                is ControllerEditorUserEvent.CanvasSizeChanged -> onCanvasSizeChanged(event.size)
            }
        }
    }

    private fun onElementClicked(id: String) {
        _state.update { it.copy(selectedElementId = id) }
    }

    private fun onElementModified(element: ControllerElementModel) {
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

    private fun onClearSelection() {
        _state.update { it.copy(selectedElementId = null) }
    }

    private fun onOrientationChanges() {
        _state.update {
            val newOrientation = it.orientation.changeOrientation()
            val newElementsList = it.elements.map { element -> element.changeOrientation() }

            it.copy(
                orientation = newOrientation,
                elements = newElementsList,
            )
        }

        LoggerUtil.d(TAG, "Controller orientation changed to ${_state.value.orientation}")
    }

    private fun ControllerElementModel.changeOrientation(): ControllerElementModel {
        val canvasSizeDp = _state.value.canvasSizeDp
        val elementsWidthDp = canvasSizeDp.width * this.displayParameters.scaleSize.width
        val elementsHeightDp = canvasSizeDp.height * this.displayParameters.scaleSize.height

        return this.changeDisplayParameters(
            this.displayParameters.copy(
                scaleSize =
                    Size(
                        width = elementsHeightDp / canvasSizeDp.width,
                        height = elementsWidthDp / canvasSizeDp.height,
                    ),
            ),
        )
    }

    private suspend fun onSaveController() {
        val canvasSizeDp = _state.value.canvasSizeDp

        saveControllerUseCase.execute(
            ControllerModel(
                orientation = _state.value.orientation,
                canvasRatio = canvasSizeDp.width / canvasSizeDp.height,
                elements = _state.value.elements,
            ),
        )

        _events.emit(ControllerEditorNavigation.Close)
    }

    private fun onCanvasSizeChanged(size: Size) {
        LoggerUtil.d(TAG, "Canvas size changed to $size")
        _state.update { it.copy(canvasSizeDp = size) }
    }

    companion object {
        private const val TAG = "ControllerEditorViewModel"
    }
}
