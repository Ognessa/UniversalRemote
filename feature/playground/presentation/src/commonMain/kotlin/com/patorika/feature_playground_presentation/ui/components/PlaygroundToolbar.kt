package com.patorika.feature_playground_presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_playground_presentation.model.PlaygroundUserEvent
import org.jetbrains.compose.resources.painterResource
import universalremote.core.generated.resources.ic_arrow_left
import universalremote.core.generated.resources.ic_bluetooth
import universalremote.core.generated.resources.ic_error
import universalremote.core.generated.resources.ic_pointer_right
import universalremote.core.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaygroundToolbar(
    connectedDeviceName: String? = null,
    deviceConnectionStatus: DeviceConnectionState,
    onEvent: (PlaygroundUserEvent) -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { onEvent(PlaygroundUserEvent.Close) },
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_arrow_left),
                        contentDescription = null,
                    )
                }

                ConnectedDeviceLabel(
                    name = connectedDeviceName,
                    status = deviceConnectionStatus,
                    onClick = { onEvent(PlaygroundUserEvent.OpenDevicePicker) },
                )

                Box(modifier = Modifier.minimumInteractiveComponentSize())
            }
        },
    )
}

@Composable
private fun ConnectedDeviceLabel(
    name: String?,
    status: DeviceConnectionState,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .clip(RoundedCornerShape(40f))
                .clickable { onClick() }
                .padding(CoreDimens.current.standardContentInterval),
        horizontalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BluetoothDeviceStatusUi(status)

        Text(
            modifier = Modifier.width(100.dp),
            text = name ?: "Pick device",
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
        )

        Icon(
            painter = painterResource(CoreRes.drawable.ic_pointer_right),
            contentDescription = null,
        )
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
