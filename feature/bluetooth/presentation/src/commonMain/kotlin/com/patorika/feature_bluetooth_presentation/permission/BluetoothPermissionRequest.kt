package com.patorika.feature_bluetooth_presentation.permission

import androidx.compose.runtime.Composable

@Composable
expect fun BluetoothPermissionRequest(onResult: (Boolean) -> Unit)
