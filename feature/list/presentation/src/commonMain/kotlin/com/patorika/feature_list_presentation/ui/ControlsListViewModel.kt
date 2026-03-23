package com.patorika.feature_list_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.TextProvider
import com.patorika.feature_controller.main.model.ControllerModel
import com.patorika.feature_list_presentation.model.ControlsListEvents
import com.patorika.feature_list_presentation.model.ControlsListScreenState
import com.patorika.feature_list_presentation.usecase.GetAllControllersUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import universalremote.feature_list_presentation.generated.resources.Res
import universalremote.feature_list_presentation.generated.resources.controls_list_fetching_error

class ControlsListViewModel(
    private val appNotificationManager: AppNotificationManager,
    private val getAllControllersUseCase: GetAllControllersUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ControlsListScreenState())
    val state: StateFlow<ControlsListScreenState> = _state

    init {
        fetchControllers()
    }

    private fun fetchControllers() {
        viewModelScope.launch {
            getAllControllersUseCase.execute().collectLatest { result ->
                result
                    .onSuccess(::onFetchListSuccess)
                    .onFailure(::onFetchListFailure)
            }
        }
    }

    private fun onFetchListSuccess(list: List<ControllerModel>) {
        _state.update { it.copy(controllersList = list) }
    }

    private fun onFetchListFailure(error: Throwable) {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.controls_list_fetching_error),
                ),
            )
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

    companion object {
        private const val TAG = "ControlsListViewModel"
    }
}
