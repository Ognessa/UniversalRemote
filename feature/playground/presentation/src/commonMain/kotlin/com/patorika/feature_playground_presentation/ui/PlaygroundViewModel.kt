package com.patorika.feature_playground_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.util.LoggerUtil
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_playground_presentation.model.PlaygroundNavigationEvent
import com.patorika.feature_playground_presentation.model.PlaygroundScreenState
import com.patorika.feature_playground_presentation.model.PlaygroundUserEvent
import com.patorika.feature_playground_presentation.useCase.GetControllerByIdUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaygroundViewModel(
    private val controllerId: String,
    private val getControllerByIdUseCase: GetControllerByIdUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(PlaygroundScreenState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<PlaygroundNavigationEvent>()
    val event = _event.asSharedFlow()

    init {
        loadControllerDetails()
    }

    private fun loadControllerDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getControllerByIdUseCase
                .execute(controllerId)
                .fold(
                    onSuccess = ::handleFetchControllerSuccess,
                    onFailure = ::handleFetchControllerFailure,
                )
        }
    }

    private fun handleFetchControllerSuccess(model: ControllerModel) {
        _state.update {
            it.copy(
                controller = model,
                isLoading = false,
            )
        }
    }

    private fun handleFetchControllerFailure(exception: Throwable) {
        _state.update { it.copy(isLoading = false) }
    }

    fun onEvent(event: PlaygroundUserEvent) {
        when (event) {
            is PlaygroundUserEvent.Close -> {
                handleNavigationEvent(PlaygroundNavigationEvent.Close)
            }

            is PlaygroundUserEvent.OpenDevicePicker -> {
                handleNavigationEvent(
                    PlaygroundNavigationEvent.OpenDevicePicker,
                )
            }

            is PlaygroundUserEvent.ElementAction -> {
                onElementActionActivated(event.action)
            }

            is PlaygroundUserEvent.ElementModified -> {
                onElementModified(event.element)
            }
        }
    }

    private fun onElementActionActivated(action: String) {
        // TODO send action to the device
        LoggerUtil.d(TAG, "Element action called: $action")
    }

    private fun onElementModified(element: ControllerElementModel) {
        _state.update { currentState ->
            currentState.controller?.let { controller ->
                val elements =
                    controller.elements.map { currentElement ->
                        if (currentElement.id == element.id) {
                            element
                        } else {
                            currentElement
                        }
                    }

                currentState.copy(
                    controller = controller.copy(elements = elements),
                )
            } ?: currentState
        }
    }

    private fun handleNavigationEvent(event: PlaygroundNavigationEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    companion object {
        private const val TAG = "PlaygroundViewModel"
    }
}
