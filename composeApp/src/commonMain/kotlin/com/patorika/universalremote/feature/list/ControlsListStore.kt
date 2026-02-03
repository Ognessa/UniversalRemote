package com.patorika.universalremote.feature.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ControlsListStore(
    private val scope: CoroutineScope,
) {
    private val _state = MutableStateFlow(ControlsListScreenState())
    val state: StateFlow<ControlsListScreenState> = _state

    fun onEvent(event: ControlsListEvents) {
        when (event) {
            is ControlsListEvents.CreateNewControl -> {
                // TODO
            }
        }
    }
}
