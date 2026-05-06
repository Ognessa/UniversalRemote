package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_bluetooth_presentation.util.rememberOpenAppSettings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import universalremote.core.generated.resources.ic_bluetooth
import universalremote.feature_bluetooth_presentation.generated.resources.Res
import universalremote.feature_bluetooth_presentation.generated.resources.bluetooth_permission_denied_message
import universalremote.feature_bluetooth_presentation.generated.resources.bluetooth_permission_denied_title
import universalremote.feature_bluetooth_presentation.generated.resources.bluetooth_permission_tap_label
import universalremote.core.generated.resources.Res as CoreRes

@Composable
internal fun BluetoothPermissionDeniedContent(modifier: Modifier = Modifier) {
    val openAppSettings = rememberOpenAppSettings()

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
            tint = MaterialTheme.colorScheme.error,
        )

        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.bluetooth_permission_denied_title),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.bluetooth_permission_denied_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { openAppSettings() }
                    .padding(vertical = CoreDimens.current.standardContentInterval),
            text = stringResource(Res.string.bluetooth_permission_tap_label),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}
