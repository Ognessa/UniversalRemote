package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_bluetooth
import universalremote.feature_bluetooth_presentation.generated.resources.Res
import universalremote.feature_bluetooth_presentation.generated.resources.bluetooth_disabled_message
import universalremote.feature_bluetooth_presentation.generated.resources.bluetooth_disabled_title
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun BluetoothDisabledContent(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(32.dp),
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
            text = stringResource(Res.string.bluetooth_disabled_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.bluetooth_disabled_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
