package com.patorika.feature_bluetooth_presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patorika.core.ui.theme.CoreDimens
import com.patorika.feature_bluetooth_presentation.model.BluetoothDeviceModel
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesEvents

@Composable
internal fun BluetoothDevicesBody(
    modifier: Modifier = Modifier,
    devices: List<BluetoothDeviceModel>,
    onEvent: (BluetoothDevicesEvents) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(CoreDimens.current.standardContentPadding),
        verticalArrangement = Arrangement.spacedBy(CoreDimens.current.standardContentInterval),
    ) {
        items(devices, key = { it.address }) { device ->
            BluetoothDeviceItemUi(
                modifier = Modifier.fillMaxWidth(),
                device = device,
                onClick = { onEvent(BluetoothDevicesEvents.DeviceClicked(device.address)) },
            )
        }
    }
}
