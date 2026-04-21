package com.patorika.feature_bluetooth_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesEvents
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesNavigation
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesScreenState
import com.patorika.feature_bluetooth_presentation.usecase.GetBluetoothDevicesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BluetoothDevicesViewModel(
    private val getBluetoothDevicesUseCase: GetBluetoothDevicesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(BluetoothDevicesScreenState())
    val state: StateFlow<BluetoothDevicesScreenState> = _state

    private val _events = MutableSharedFlow<BluetoothDevicesNavigation>()
    val events = _events.asSharedFlow()

    init {
        loadDevices()
    }

    fun onEvent(event: BluetoothDevicesEvents) {
        when (event) {
            is BluetoothDevicesEvents.Close -> {
                emitNavigationEvent(BluetoothDevicesNavigation.Close)
            }

            is BluetoothDevicesEvents.Refresh -> {
                loadDevices()
            }

            is BluetoothDevicesEvents.DeviceClicked -> {
                emitNavigationEvent(
                    BluetoothDevicesNavigation.DeviceSelected(event.address),
                )
            }
        }
    }

    private fun loadDevices() {
        viewModelScope.launch {
            setLoading(true)
            getBluetoothDevicesUseCase
                .execute()
                .onSuccess { devices -> _state.update { it.copy(devices = devices) } }
                .also { setLoading(false) }
        }
    }

    private fun emitNavigationEvent(event: BluetoothDevicesNavigation) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }
}
