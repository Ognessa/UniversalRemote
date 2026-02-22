package com.patorika.feature_library_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.model.controller.ControllerModel
import com.patorika.feature_editor_api.state.EditorSharedState
import com.patorika.feature_library_presentation.model.EditorLibraryNavigation
import com.patorika.feature_library_presentation.model.EditorLibraryUserEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class EditorLibraryViewModel(
    private val editorSharedState: EditorSharedState,
) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<EditorLibraryNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onUserEvent(event: EditorLibraryUserEvents) {
        viewModelScope.launch {
            when (event) {
                is EditorLibraryUserEvents.ElementSelected -> handleSelectedElement(event.element)
            }
        }
    }

    private suspend fun handleSelectedElement(element: ControllerModel) {
        editorSharedState.emitNewElement(element)
        _navigationEvent.emit(EditorLibraryNavigation.CloseLibrary)
    }
}
