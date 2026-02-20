package com.patorika.feature_list_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.feature_list_presentation.model.ControlsListEvents
import com.patorika.feature_list_presentation.model.ControlsListScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ControlsListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ControlsListScreenState())
    val state: StateFlow<ControlsListScreenState> = _state

    fun onEvent(event: ControlsListEvents) {
        when (event) {
            is ControlsListEvents.Refresh -> refreshScreenContent()
            is ControlsListEvents.CreateNewControl -> addNewRandomController()
        }
    }

    private fun refreshScreenContent() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(3000)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun addNewRandomController() {
        _state.update {
            val list = it.controllersList.toMutableList()
            list.add("Controller ${list.size}")
            it.copy(controllersList = list)
        }
    }
}
