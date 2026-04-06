package com.patorika.feature_editor_presentation.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.TextProvider
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.ext.generateControllerId
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_controller.presentation.model.ControllerOrientation
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_editor_presentation.model.ControllerEditorNavigation
import com.patorika.feature_editor_presentation.model.ControllerEditorParams
import com.patorika.feature_editor_presentation.model.ControllerEditorScreenState
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.CanvasSizeChanged
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.ClearSelection
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.ElementAction
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.OpenLibrary
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.OrientationChanged
import com.patorika.feature_editor_presentation.model.ControllerEditorUserEvent.Save
import com.patorika.feature_editor_presentation.usecase.GetControllerByIdUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import universalremote.feature_editor_presentation.generated.resources.Res
import universalremote.feature_editor_presentation.generated.resources.editor_cant_duplicate_element
import universalremote.feature_editor_presentation.generated.resources.editor_cant_load_controller
import universalremote.feature_editor_presentation.generated.resources.editor_cant_save_empty_controller

class ControllerEditorViewModel(
    private val params: ControllerEditorParams,
    private val appNotificationManager: AppNotificationManager,
    private val editorSharedState: EditorSharedState,
    private val getControllerByIdUseCase: GetControllerByIdUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ControllerEditorScreenState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ControllerEditorNavigation>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            initEditedController()
            observeNewElements()
        }
    }

    private suspend fun initEditedController() {
        if (params.id != null) {
            _state.update { it.copy(isLoading = true) }
            getControllerByIdUseCase.execute(params.id).fold(
                onSuccess = ::initEditedControllerSuccess,
                onFailure = ::initEditedControllerFailure,
            )
        }
    }

    private fun initEditedControllerSuccess(model: ControllerModel) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    title = model.name,
                    orientation = model.orientation,
                    elements = model.elements,
                )
            }
        }
    }

    private fun initEditedControllerFailure(error: Throwable) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = false) }
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.editor_cant_load_controller),
                ),
            )
        }
    }

    private suspend fun observeNewElements() {
        editorSharedState.newElementsFlow.collectLatest { newElements ->
            _state.update { currentState ->
                val normalizedElements = newElements.normalizeReceivedElements(currentState)
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

    private fun List<ControllerElementModel>.normalizeReceivedElements(
        currentState: ControllerEditorScreenState,
    ): List<ControllerElementModel> =
        this.map { element ->
            if (currentState.elements.none { it.id == element.id }) {
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

    fun onEvent(event: ControllerEditorUserEvent) {
        viewModelScope.launch {
            when (event) {
                is ClearSelection -> onClearSelection()
                is OrientationChanged -> onOrientationChanges()
                is Save -> onSaveController()
                is OpenLibrary -> emitNavigateEvent(ControllerEditorNavigation.OpenLibrary)
                is CanvasSizeChanged -> onCanvasSizeChanged(event.size)
                is ElementAction -> onElementConfigEvent(event)
            }
        }
    }

    private fun onElementConfigEvent(event: ElementAction) {
        when (event) {
            is ElementAction.Clicked -> onElementClicked(event.id)
            is ElementAction.Modified -> onElementModified(event.element)
            is ElementAction.OpenSignalEditor -> openSignalEditor(event.id)
            is ElementAction.Duplicate -> onDuplicateElement(event.id)
            is ElementAction.Delete -> onDeleteElement(event.id)
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

    private suspend fun onSaveController() {
        if (_state.value.elements.isNotEmpty()) {
            val canvasSizeDp = _state.value.canvasSizeDp

            val controllerModel =
                ControllerModel(
                    id = params.id ?: generateControllerId(),
                    name = _state.value.title,
                    orientation = _state.value.orientation,
                    canvasRatio = canvasSizeDp.width / canvasSizeDp.height,
                    elements = _state.value.elements,
                )

            _events.emit(ControllerEditorNavigation.OpenTitleEditor(controllerModel))
        } else {
            showEmptyControllerError()
        }
    }

    private suspend fun showEmptyControllerError() {
        appNotificationManager.send(
            AppNotification.SnackBar(
                message = TextProvider.Res(Res.string.editor_cant_save_empty_controller),
            ),
        )
    }

    private fun onCanvasSizeChanged(size: Size) {
        LoggerUtil.d(TAG, "Canvas size changed to $size")
        _state.update { it.copy(canvasSizeDp = size) }
    }

    private fun emitNavigateEvent(event: ControllerEditorNavigation) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun openSignalEditor(id: String) {
        viewModelScope.launch {
            _state.value.elements
                .firstOrNull { id == it.id }
                ?.let { element ->
                    emitNavigateEvent(ControllerEditorNavigation.OpenSignalEditor(element))
                }
        }
    }

    private fun onDuplicateElement(id: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                val selectedElement = currentState.elements.firstOrNull { it.id == id }
                if (selectedElement != null) {
                    // copy element with basic position
                    val copyElement =
                        selectedElement.createElementWithNewId().let {
                            val newDisplayParam =
                                it.displayParameters.copy(
                                    centerOffset = Offset(0.5f, 0.5f),
                                )
                            it.changeDisplayParameters(newDisplayParam)
                        }

                    currentState.copy(
                        elements = currentState.elements.toMutableList().apply { add(copyElement) },
                        selectedElementId = copyElement.id,
                    )
                } else {
                    // show error
                    appNotificationManager.send(
                        AppNotification.SnackBar(
                            message = TextProvider.Res(Res.string.editor_cant_duplicate_element),
                        ),
                    )
                    currentState
                }
            }
        }
    }

    private fun onDeleteElement(id: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    elements = currentState.elements.filter { element -> element.id != id },
                    selectedElementId = null,
                )
            }
        }
    }

    companion object {
        private const val TAG = "ControllerEditorViewModel"
    }
}
