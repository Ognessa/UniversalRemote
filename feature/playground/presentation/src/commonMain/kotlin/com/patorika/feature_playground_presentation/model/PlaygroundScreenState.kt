package com.patorika.feature_playground_presentation.model

import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_controller.presentation.model.ControllerModel

data class PlaygroundScreenState(
    val isLoading: Boolean = false,
    val isBluetoothEnabled: Boolean = false,
    val controller: ControllerModel? = null,
    val connectionState: DeviceConnectionState = DeviceConnectionState.Idle,
    val connectedDevice: BluetoothDevice? = null,
)
