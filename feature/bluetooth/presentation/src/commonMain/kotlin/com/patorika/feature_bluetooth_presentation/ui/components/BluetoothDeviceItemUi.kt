package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patorika.feature_bluetooth_presentation.model.BluetoothDeviceModel

@Composable
internal fun BluetoothDeviceItemUi(
    modifier: Modifier = Modifier,
    device: BluetoothDeviceModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = device.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
