package com.patorika.feature_bluetooth_presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun BluetoothPermissionRequest(onResult: (Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        onResult(true)
    }
}
