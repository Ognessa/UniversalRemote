package com.patorika.feature_signal_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.feature_controller.presentation.elements.basic.model.ControllerElementModel
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_signal_presentation.model.SignalEditorScreenEvent
import com.patorika.feature_signal_presentation.model.SignalEditorScreenNavigation
import com.patorika.feature_signal_presentation.model.SignalEditorScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignalEditorViewModel(
    elementData: ControllerElementModel,
    private val editorSharedState: EditorSharedState,
) : ViewModel() {
    private val _state = MutableStateFlow(SignalEditorScreenState(elementData))
    val state: StateFlow<SignalEditorScreenState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignalEditorScreenNavigation>()
    val navigationEvent: SharedFlow<SignalEditorScreenNavigation> = _navigationEvent.asSharedFlow()

    fun onEvent(event: SignalEditorScreenEvent) {
        when (event) {
            is SignalEditorScreenEvent.OnElementModified -> {
                onElementModified(event.element)
            }

            is SignalEditorScreenEvent.SaveChanges -> {
                saveChanges()
            }
        }
    }

    private fun onElementModified(element: ControllerElementModel) {
        _state.update { current -> current.copy(element = element) }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            editorSharedState.emitElement(_state.value.element)
            _navigationEvent.emit(SignalEditorScreenNavigation.Close)
        }
    }
}
