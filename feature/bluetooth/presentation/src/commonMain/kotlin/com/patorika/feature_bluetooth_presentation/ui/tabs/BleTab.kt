package com.patorika.feature_bluetooth_presentation.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_bluetooth_manager.BluetoothManager.Companion.BLE_SCAN_DURATION
import com.patorika.feature_bluetooth_manager.model.ScanState
import com.patorika.feature_bluetooth_presentation.Res
import com.patorika.feature_bluetooth_presentation.ble_scan_error
import com.patorika.feature_bluetooth_presentation.model.DevicePickerEvents
import com.patorika.feature_bluetooth_presentation.model.DevicePickerUiState
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothDeviceItemUi
import com.patorika.feature_bluetooth_presentation.ui.components.ScanButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BleTab(
    modifier: Modifier = Modifier,
    state: DevicePickerUiState,
    onEvent: (DevicePickerEvents) -> Unit,
) {
    val padding = CoreDimens.current.standardContentPadding
    val interval = CoreDimens.current.standardContentInterval

    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(padding),
            verticalArrangement = Arrangement.spacedBy(interval),
        ) {
            item {
                Column {
                    if (state.scanState.ble is ScanState.Error) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text =
                                stringResource(
                                    Res.string.ble_scan_error,
                                    state.scanState.ble.message,
                                ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            items(
                items = state.devices.ble,
                key = { it.id },
            ) { device ->
                BluetoothDeviceItemUi(
                    modifier = Modifier.fillMaxWidth(),
                    device = device,
                    status = state.checkDeviceConnectionState(device),
                    onClick = { onEvent(DevicePickerEvents.ConnectToDevice(device)) },
                )
            }
        }

        ScanButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(CoreDimens.current.standardContentPadding),
            scanState = state.scanState.ble,
            durationSeconds = BLE_SCAN_DURATION,
            onStartScan = { onEvent(DevicePickerEvents.StartBleScan) },
        )
    }
}
