package com.patorika.feature_bluetooth_manager.model

sealed interface DeviceConnectionState {
    data object Idle : DeviceConnectionState

    data object Connecting : DeviceConnectionState

    data object Connected : DeviceConnectionState

    data object Disconnecting : DeviceConnectionState

    data class Reconnecting(
        val attempt: Int,
    ) : DeviceConnectionState

    data class Error(
        val message: String,
    ) : DeviceConnectionState
}
