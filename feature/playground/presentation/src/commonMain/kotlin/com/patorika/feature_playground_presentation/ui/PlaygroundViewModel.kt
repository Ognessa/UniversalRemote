package com.patorika.feature_playground_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_playground_presentation.model.BluetoothState
import com.patorika.feature_playground_presentation.model.PlaygroundNavigationEvent
import com.patorika.feature_playground_presentation.model.PlaygroundScreenState
import com.patorika.feature_playground_presentation.model.PlaygroundUserEvent
import com.patorika.feature_playground_presentation.useCase.GetControllerByIdUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaygroundViewModel(
    private val controllerId: String,
    private val getControllerByIdUseCase: GetControllerByIdUseCase,
    private val bluetoothManager: BluetoothManager,
) : ViewModel() {
    private val _state = MutableStateFlow(PlaygroundScreenState())
    val state: StateFlow<PlaygroundScreenState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<PlaygroundNavigationEvent>()
    val event: SharedFlow<PlaygroundNavigationEvent> = _event.asSharedFlow()

    init {
        loadControllerDetails()
        observeBluetoothState()
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

    private fun observeBluetoothState() {
        viewModelScope.launch {
            combine(
                bluetoothManager.isBluetoothEnabled,
                bluetoothManager.connectedDevice,
                bluetoothManager.connectionState,
                ::BluetoothState,
            ).collectLatest { bluetoothState ->
                _state.update { it.copy(bluetoothState = bluetoothState) }
            }
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

    override fun onCleared() {
        super.onCleared()
        bluetoothManager.disconnect()
    }

    fun onEvent(event: PlaygroundUserEvent) {
        when (event) {
            is PlaygroundUserEvent.Close -> {
                handleNavigationEvent(PlaygroundNavigationEvent.Close)
            }

            is PlaygroundUserEvent.OpenDevicePicker -> {
                handleNavigationEvent(PlaygroundNavigationEvent.OpenDevicePicker)
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
        bluetoothManager.sendSignal(action)
        Logger.d(tag = TAG) { "Element action called: $action" }
    }

    private fun onElementModified(element: ControllerElementModel) {
        _state.update { currentState ->
            currentState.controller?.let { controller ->
                val elements =
                    controller.elements.map { currentElement ->
                        if (currentElement.id == element.id) element else currentElement
                    }
                currentState.copy(controller = controller.copy(elements = elements))
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
