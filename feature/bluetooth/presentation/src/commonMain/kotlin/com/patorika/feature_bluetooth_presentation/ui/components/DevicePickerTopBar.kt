package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.patorika.feature_bluetooth_presentation.Res
import com.patorika.feature_bluetooth_presentation.bluetooth_devices_screen_title
import com.patorika.feature_bluetooth_presentation.model.DevicePickerEvents
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_arrow_left
import universalremote.core.generated.resources.Res as CoreRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DevicePickerTopBar(onEvent: (DevicePickerEvents) -> Unit) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { onEvent(DevicePickerEvents.Close) },
                ) {
                    Icon(
                        painter = painterResource(CoreRes.drawable.ic_arrow_left),
                        contentDescription = null,
                    )
                }

                Text(
                    stringResource(Res.string.bluetooth_devices_screen_title),
                    style = MaterialTheme.typography.titleLarge,
                )

                Box(modifier = Modifier.minimumInteractiveComponentSize())
            }
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}
