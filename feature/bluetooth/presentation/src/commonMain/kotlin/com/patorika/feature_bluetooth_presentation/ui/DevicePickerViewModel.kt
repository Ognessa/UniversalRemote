package com.patorika.feature_bluetooth_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesNavigation
import com.patorika.feature_bluetooth_presentation.model.ConnectionState
import com.patorika.feature_bluetooth_presentation.model.DeviceListState
import com.patorika.feature_bluetooth_presentation.model.DevicePickerEvents
import com.patorika.feature_bluetooth_presentation.model.DevicePickerUiState
import com.patorika.feature_bluetooth_presentation.model.ScanStateModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DevicePickerViewModel(
    private val bluetoothManager: BluetoothManager,
) : ViewModel() {
    private val deviceListState =
        combine(
            bluetoothManager.pairedDevices,
            bluetoothManager.discoveredClassicDevices,
            bluetoothManager.bleDevices,
            ::DeviceListState,
        )

    private val scanState =
        combine(
            bluetoothManager.classicScanState,
            bluetoothManager.bleScanState,
            ::ScanStateModel,
        )

    private val connectionState =
        combine(
            bluetoothManager.connectedDevice,
            bluetoothManager.connectionState,
            ::ConnectionState,
        )

    val state: StateFlow<DevicePickerUiState> =
        combine(
            bluetoothManager.isBluetoothEnabled,
            deviceListState,
            scanState,
            connectionState,
        ) { isEnabled, devices, scan, connection ->
            buildUiState(isEnabled, devices, scan, connection)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DevicePickerUiState(),
        )

    private val _events = MutableSharedFlow<BluetoothDevicesNavigation>()
    val events: SharedFlow<BluetoothDevicesNavigation> = _events.asSharedFlow()

    fun onEvent(event: DevicePickerEvents) {
        when (event) {
            is DevicePickerEvents.Close -> {
                cleanup()
                emitNavigationEvent(BluetoothDevicesNavigation.Close)
            }

            is DevicePickerEvents.StartClassicScan -> {
                bluetoothManager.loadPairedDevices()
                bluetoothManager.startClassicScan()
            }

            is DevicePickerEvents.StartBleScan -> {
                bluetoothManager.startBleScan()
            }

            is DevicePickerEvents.ConnectToDevice -> {
                bluetoothManager.connectToDevice(event.device)
            }

            is DevicePickerEvents.Disconnect -> {
                bluetoothManager.disconnect()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }

    private fun cleanup() {
        bluetoothManager.stopClassicScan()
        bluetoothManager.stopBleScan()
        bluetoothManager.clearDiscoveredDevices()
    }

    private fun buildUiState(
        isBluetoothEnabled: Boolean,
        devices: DeviceListState,
        scanState: ScanStateModel,
        connectionState: ConnectionState,
    ): DevicePickerUiState {
        val connectedId = connectionState.device?.id

        fun List<BluetoothDevice>.withConnectedFirst(): List<BluetoothDevice> =
            if (connectedId == null) {
                this
            } else {
                sortedByDescending { it.id == connectedId }
            }

        return DevicePickerUiState(
            isBluetoothEnabled = isBluetoothEnabled,
            devices =
                devices.copy(
                    paired = devices.paired.withConnectedFirst(),
                    discoveredClassic = devices.discoveredClassic.withConnectedFirst(),
                    ble = devices.ble.withConnectedFirst(),
                ),
            scanState = scanState,
            connectionState = connectionState,
        )
    }

    private fun emitNavigationEvent(event: BluetoothDevicesNavigation) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
