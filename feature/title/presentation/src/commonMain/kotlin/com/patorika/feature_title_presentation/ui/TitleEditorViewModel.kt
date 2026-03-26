package com.patorika.feature_title_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.core.provider.notification.manager.AppNotificationManager
import com.patorika.core.provider.notification.model.AppNotification
import com.patorika.core.provider.text.TextProvider
import com.patorika.feature_title_api.TitleEditorNavArgs
import com.patorika.feature_title_presentation.model.TitleEditorEvents
import com.patorika.feature_title_presentation.model.TitleEditorNavigation
import com.patorika.feature_title_presentation.model.TitleEditorScreenState
import com.patorika.feature_title_presentation.useCase.SaveControllerUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import universalremote.feature_title_presentation.generated.resources.Res
import universalremote.feature_title_presentation.generated.resources.editor_cant_save_changes
import universalremote.feature_title_presentation.generated.resources.title_cant_be_empty

class TitleEditorViewModel(
    private val args: TitleEditorNavArgs,
    private val appNotificationManager: AppNotificationManager,
    private val saveControllerUseCase: SaveControllerUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TitleEditorScreenState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<TitleEditorNavigation>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(title = args.model.name) }
        }
    }

    fun onEvent(event: TitleEditorEvents) =
        when (event) {
            is TitleEditorEvents.TitleChanged -> onTitleChanged(event.value)
            is TitleEditorEvents.Save -> onSave()
            is TitleEditorEvents.Cancel -> onCancel()
        }

    private fun onTitleChanged(title: String) {
        viewModelScope.launch {
            _state.update { it.copy(title = title.trim()) }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val title = _state.value.title
            if (title.isNotBlank()) {
                saveControllerUseCase
                    .execute(args.model.copy(name = title))
                    .onSuccess { emitNavigateEvent(TitleEditorNavigation.from(args.successNavigation)) }
                    .onFailure { showSaveError() }
            } else {
                emptyTitleError()
            }
        }
    }

    private fun showSaveError() {
        viewModelScope.launch {
            appNotificationManager.send(
                AppNotification.SnackBar(
                    message = TextProvider.Res(Res.string.editor_cant_save_changes),
                ),
            )
        }
    }

    private suspend fun emptyTitleError() {
        appNotificationManager.send(
            AppNotification.SnackBar(
                message = TextProvider.Res(Res.string.title_cant_be_empty),
            ),
        )
    }

    private fun onCancel() {
        viewModelScope.launch {
            emitNavigateEvent(TitleEditorNavigation.Close)
        }
    }

    private suspend fun emitNavigateEvent(event: TitleEditorNavigation) = _events.emit(event)
}
