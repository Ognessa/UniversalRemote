package com.patorika.feature_list_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.TextProvider
import com.patorika.feature_controller.presentation.model.ControllerModel
import com.patorika.feature_list_presentation.model.ControlsListEvents
import com.patorika.feature_list_presentation.model.ControlsListEvents.CreateNew
import com.patorika.feature_list_presentation.model.ControlsListEvents.Item
import com.patorika.feature_list_presentation.model.ControlsListEvents.Refresh
import com.patorika.feature_list_presentation.model.ControlsListNavigation
import com.patorika.feature_list_presentation.model.ControlsListScreenState
import com.patorika.feature_list_presentation.usecase.DeleteControllerUseCase
import com.patorika.feature_list_presentation.usecase.DuplicateControllerUseCase
import com.patorika.feature_list_presentation.usecase.GetAllControllersUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import universalremote.feature_list_presentation.generated.resources.Res
import universalremote.feature_list_presentation.generated.resources.controller_delete_failure_message
import universalremote.feature_list_presentation.generated.resources.controller_delete_success_message
import universalremote.feature_list_presentation.generated.resources.controller_duplicate_failure_message
import universalremote.feature_list_presentation.generated.resources.controller_duplicate_success_message
import universalremote.feature_list_presentation.generated.resources.controller_not_exists_message
import universalremote.feature_list_presentation.generated.resources.controls_list_fetching_error

class ControlsListViewModel(
    private val appNotificationManager: AppNotificationManager,
    private val getAllControllersUseCase: GetAllControllersUseCase,
    private val duplicateControllerUseCase: DuplicateControllerUseCase,
    private val deleteControllerUseCase: DeleteControllerUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ControlsListScreenState())
    val state: StateFlow<ControlsListScreenState> = _state

    private val _events = MutableSharedFlow<ControlsListNavigation>()
    val events = _events.asSharedFlow()

    init {
        refreshScreenContent()
    }

    fun onEvent(event: ControlsListEvents) {
        when (event) {
            is Refresh -> refreshScreenContent()
            is CreateNew -> emitNavigationEvent(ControlsListNavigation.OpenEditor())
            is Item -> onItemEvent(event)
        }
    }

    private fun onItemEvent(events: Item) {
        when (events) {
            is Item.Clicked -> emitNavigationEvent(ControlsListNavigation.OpenPlayground(events.id))
            is Item.Rename -> onRename(events.id)
            is Item.Edit -> emitNavigationEvent(ControlsListNavigation.OpenEditor(events.id))
            is Item.Duplicate -> onDuplicate(events.id)
            is Item.Delete -> onDelete(events.id)
        }
    }

    private fun refreshScreenContent() {
        viewModelScope.launch {
            setLoading(true)
            fetchControllers()
        }
    }

    private suspend fun fetchControllers() {
        getAllControllersUseCase.execute().collectLatest { result ->
            result
                .onSuccess(::onFetchListSuccess)
                .onFailure(::onFetchListFailure)
        }
    }

    private fun onFetchListSuccess(list: List<ControllerModel>) {
        _state.update { it.copy(controllersList = list) }
        setLoading(false)
    }

    private fun onFetchListFailure(error: Throwable) {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.controls_list_fetching_error),
                ),
            )
        }
        setLoading(false)
    }

    private fun onRename(id: String) {
        viewModelScope.launch {
            val model = _state.value.controllersList.firstOrNull { it.id == id }
            if (model != null) {
                emitNavigationEvent(ControlsListNavigation.OpenTitleEditor(model))
            } else {
                controllerNotExistsError()
            }
        }
    }

    private suspend fun controllerNotExistsError() {
        appNotificationManager.send(
            AppNotification.SnackBar(
                message = TextProvider.Res(Res.string.controller_not_exists_message),
            ),
        )
    }

    private fun onDuplicate(id: String) {
        viewModelScope.launch {
            val model = _state.value.controllersList.firstOrNull { it.id == id }
            if (model != null) {
                duplicateControllerUseCase.execute(model).fold(
                    onSuccess = ::duplicateSuccess,
                    onFailure = ::duplicateFailure,
                )
            } else {
                duplicateFailure(null)
            }
        }
    }

    private fun duplicateSuccess(unit: Unit) {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.controller_duplicate_success_message),
                ),
            )
        }
    }

    private fun duplicateFailure(error: Throwable?) {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.controller_duplicate_failure_message),
                ),
            )
        }
    }

    private fun onDelete(id: String) {
        viewModelScope.launch {
            deleteControllerUseCase.execute(id).fold(
                onSuccess = ::deleteSuccess,
                onFailure = ::deleteFailure,
            )
        }
    }

    private fun deleteSuccess(unit: Unit) {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.controller_delete_success_message),
                ),
            )
        }
    }

    private fun deleteFailure(error: Throwable) {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.controller_delete_failure_message),
                ),
            )
        }
    }

    private fun emitNavigationEvent(event: ControlsListNavigation) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    companion object {
        private const val TAG = "ControlsListViewModel"
    }
}
