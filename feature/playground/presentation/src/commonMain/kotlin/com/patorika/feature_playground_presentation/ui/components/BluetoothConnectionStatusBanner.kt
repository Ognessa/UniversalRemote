package com.patorika.feature_playground_presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_playground_presentation.Res
import com.patorika.feature_playground_presentation.bluetooth_connection_error
import com.patorika.feature_playground_presentation.bluetooth_device_disconnected
import com.patorika.feature_playground_presentation.bluetooth_disabled_banner
import com.patorika.feature_playground_presentation.model.PlaygroundScreenState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_bluetooth
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun BluetoothConnectionStatusBanner(
    modifier: Modifier = Modifier,
    state: PlaygroundScreenState,
) {
    val errorText =
        when {
            state.isBluetoothEnabled.not() -> {
                stringResource(Res.string.bluetooth_disabled_banner)
            }

            state.connectionState is DeviceConnectionState.Idle -> {
                stringResource(Res.string.bluetooth_device_disconnected)
            }

            state.connectionState is DeviceConnectionState.Error -> {
                stringResource(
                    Res.string.bluetooth_connection_error,
                    state.connectionState.message,
                )
            }

            else -> {
                ""
            }
        }

    if (errorText.isNotEmpty()) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.errorContainer,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(CoreRes.drawable.ic_bluetooth),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = errorText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}
