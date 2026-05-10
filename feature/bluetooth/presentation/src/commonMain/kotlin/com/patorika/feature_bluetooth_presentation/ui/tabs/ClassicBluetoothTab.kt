package com.patorika.feature_bluetooth_presentation.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_bluetooth_manager.BluetoothManager.Companion.CLASSIC_SCAN_DURATION
import com.patorika.feature_bluetooth_manager.model.ScanState
import com.patorika.feature_bluetooth_presentation.Res
import com.patorika.feature_bluetooth_presentation.classic_unavailable_message
import com.patorika.feature_bluetooth_presentation.classic_unavailable_title
import com.patorika.feature_bluetooth_presentation.model.DevicePickerEvents
import com.patorika.feature_bluetooth_presentation.model.DevicePickerUiState
import com.patorika.feature_bluetooth_presentation.section_available_devices
import com.patorika.feature_bluetooth_presentation.section_paired_devices
import com.patorika.feature_bluetooth_presentation.ui.components.BluetoothDeviceItemUi
import com.patorika.feature_bluetooth_presentation.ui.components.ScanButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_bluetooth
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun ClassicBluetoothTab(
    modifier: Modifier = Modifier,
    state: DevicePickerUiState,
    onEvent: (DevicePickerEvents) -> Unit,
) {
    if (state.scanState.classic == ScanState.Unavailable) {
        ClassicUnavailableContent(modifier = modifier)
        return
    }

    val padding = CoreDimens.current.standardContentPadding
    val interval = CoreDimens.current.standardContentInterval

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(padding),
            verticalArrangement = Arrangement.spacedBy(interval),
        ) {
            item {
                SectionHeader(text = stringResource(Res.string.section_paired_devices))
            }

            items(
                items = state.devices.paired,
                key = { it.id },
            ) { device ->
                BluetoothDeviceItemUi(
                    modifier = Modifier.fillMaxWidth(),
                    device = device,
                    status = state.checkDeviceConnectionState(device),
                    onClick = { onEvent(DevicePickerEvents.ConnectToDevice(device)) },
                )
            }

            item {
                Column {
                    Spacer(Modifier.height(8.dp))
                    SectionHeader(text = stringResource(Res.string.section_available_devices))
                    Spacer(Modifier.height(8.dp))
                }
            }

            items(
                items = state.devices.discoveredClassic,
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
            scanState = state.scanState.classic,
            durationSeconds = CLASSIC_SCAN_DURATION,
            onStartScan = { onEvent(DevicePickerEvents.StartClassicScan) },
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun ClassicUnavailableContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            painter = painterResource(CoreRes.drawable.ic_bluetooth),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.classic_unavailable_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.classic_unavailable_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
