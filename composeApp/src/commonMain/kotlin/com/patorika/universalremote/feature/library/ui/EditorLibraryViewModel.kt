package com.patorika.universalremote.feature.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.universalremote.core.model.controller.ControllerType
import com.patorika.universalremote.feature.editor.api.state.EditorSharedState
import com.patorika.universalremote.feature.library.model.EditorLibraryNavigation
import com.patorika.universalremote.feature.library.model.EditorLibraryUserEvents
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

    private suspend fun handleSelectedElement(element: ControllerType) {
        editorSharedState.emitNewElement(element)
        _navigationEvent.emit(EditorLibraryNavigation.CloseLibrary)
    }
}
