package com.patorika.feature_bluetooth_presentation.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.patorika.feature_bluetooth_presentation.Res
import com.patorika.feature_bluetooth_presentation.model.DevicePickerEvents
import com.patorika.feature_bluetooth_presentation.model.DevicePickerUiState
import com.patorika.feature_bluetooth_presentation.tab_ble
import com.patorika.feature_bluetooth_presentation.tab_classic_bluetooth
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DevicePickerBody(
    modifier: Modifier = Modifier,
    state: DevicePickerUiState,
    onEvent: (DevicePickerEvents) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        SecondaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(Res.string.tab_classic_bluetooth)) },
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(Res.string.tab_ble)) },
            )
        }

        when (selectedTab) {
            0 -> {
                ClassicBluetoothTab(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onEvent = onEvent,
                )
            }

            1 -> {
                BleTab(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onEvent = onEvent,
                )
            }
        }
    }
}
