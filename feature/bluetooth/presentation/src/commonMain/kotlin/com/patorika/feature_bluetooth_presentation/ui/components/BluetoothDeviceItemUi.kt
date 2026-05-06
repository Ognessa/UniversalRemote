package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_bluetooth
import universalremote.core.generated.resources.ic_error
import universalremote.feature_bluetooth_presentation.generated.resources.Res
import universalremote.feature_bluetooth_presentation.generated.resources.device_name_unknown
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun BluetoothDeviceItemUi(
    modifier: Modifier = Modifier,
    device: BluetoothDevice,
    status: DeviceConnectionState,
    onClick: () -> Unit,
) {
    Card(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name ?: stringResource(Res.string.device_name_unknown),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = device.id,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.width(8.dp))

            BluetoothDeviceStatusUi(status)
        }
    }
}

@Composable
private fun BluetoothDeviceStatusUi(status: DeviceConnectionState) {
    when (status) {
        is DeviceConnectionState.Connecting,
        is DeviceConnectionState.Disconnecting,
        is DeviceConnectionState.Reconnecting,
        -> {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
            )
        }

        DeviceConnectionState.Connected -> {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(CoreRes.drawable.ic_bluetooth),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        is DeviceConnectionState.Error -> {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(CoreRes.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        }

        DeviceConnectionState.Idle -> {
            Spacer(Modifier.size(20.dp))
        }
    }
}
