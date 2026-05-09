package com.patorika.feature_bluetooth_manager.model

sealed interface ScanState {
    data object Idle : ScanState

    data class Scanning(
        val remainingSeconds: Int,
    ) : ScanState

    data object Finished : ScanState

    data object Unavailable : ScanState

    data class Error(
        val message: String,
    ) : ScanState
}
