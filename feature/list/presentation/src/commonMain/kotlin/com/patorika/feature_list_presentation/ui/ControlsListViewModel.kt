package com.patorika.feature_list_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.feature_list_presentation.model.ControlsListEvents
import com.patorika.feature_list_presentation.model.ControlsListScreenState
import com.patorika.feature_list_presentation.usecase.GetAllControllersUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ControlsListViewModel(
    private val getAllControllersUseCase: GetAllControllersUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ControlsListScreenState())
    val state: StateFlow<ControlsListScreenState> = _state

    init {
        fetchControllers()
    }

    private fun fetchControllers() {
        viewModelScope.launch {
            getAllControllersUseCase.execute().collectLatest { list ->
                _state.update { it.copy(controllersList = list) }
            }
        }
    }

    fun onEvent(event: ControlsListEvents) {
        when (event) {
            is ControlsListEvents.Refresh -> refreshScreenContent()
        }
    }

    private fun refreshScreenContent() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(3000)
            _state.update { it.copy(isLoading = false) }
        }
    }
}
