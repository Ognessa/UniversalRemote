package com.patorika.feature_bluetooth_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patorika.feature_bluetooth_manager.BluetoothManager
import com.patorika.feature_bluetooth_manager.model.BluetoothDevice
import com.patorika.feature_bluetooth_manager.model.DeviceConnectionState
import com.patorika.feature_bluetooth_manager.model.ScanState
import com.patorika.feature_bluetooth_presentation.model.BluetoothDevicesNavigation
import com.patorika.feature_bluetooth_presentation.model.DevicePickerEvents
import com.patorika.feature_bluetooth_presentation.model.DevicePickerUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DevicePickerViewModel(
    private val bluetoothManager: BluetoothManager,
) : ViewModel() {
    private val _state = MutableStateFlow(DevicePickerUiState())
    val state: StateFlow<DevicePickerUiState> = _state

    private val _events = MutableSharedFlow<BluetoothDevicesNavigation>()
    val events = _events.asSharedFlow()

    init {
        observeManagerFlows()
    }

    fun onEvent(event: DevicePickerEvents) {
        when (event) {
            is DevicePickerEvents.Close -> {
                bluetoothManager.stopClassicScan()
                bluetoothManager.stopBleScan()
                bluetoothManager.clearDiscoveredDevices()
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
        bluetoothManager.stopClassicScan()
        bluetoothManager.stopBleScan()
        bluetoothManager.clearDiscoveredDevices()
    }

    private fun observeManagerFlows() {
        viewModelScope.launch {
            combine(
                bluetoothManager.isBluetoothEnabled,
                bluetoothManager.pairedDevices,
                bluetoothManager.discoveredClassicDevices,
                bluetoothManager.bleDevices,
                bluetoothManager.classicScanState,
                bluetoothManager.bleScanState,
                bluetoothManager.connectedDevice,
                bluetoothManager.connectionState,
            ) { args ->
                @Suppress("UNCHECKED_CAST")
                buildUiState(
                    isBluetoothEnabled = args[0] as Boolean,
                    pairedDevices = args[1] as List<BluetoothDevice>,
                    discoveredClassicDevices = args[2] as List<BluetoothDevice>,
                    bleDevices = args[3] as List<BluetoothDevice>,
                    classicScanState = args[4] as ScanState,
                    bleScanState = args[5] as ScanState,
                    connectedDevice = args[6] as BluetoothDevice?,
                    connectionState = args[7] as DeviceConnectionState,
                )
            }.collectLatest { _state.value = it }
        }
    }

    private fun buildUiState(
        isBluetoothEnabled: Boolean,
        pairedDevices: List<BluetoothDevice>,
        discoveredClassicDevices: List<BluetoothDevice>,
        bleDevices: List<BluetoothDevice>,
        classicScanState: ScanState,
        bleScanState: ScanState,
        connectedDevice: BluetoothDevice?,
        connectionState: DeviceConnectionState,
    ): DevicePickerUiState {
        val connectedId = connectedDevice?.id

        fun List<BluetoothDevice>.withConnectedFirst(): List<BluetoothDevice> {
            if (connectedId == null) return this
            val target = firstOrNull { it.id == connectedId } ?: return this
            return listOf(target) + filter { it.id != connectedId }
        }

        return DevicePickerUiState(
            isBluetoothEnabled = isBluetoothEnabled,
            pairedDevices = pairedDevices.withConnectedFirst(),
            discoveredClassicDevices = discoveredClassicDevices.withConnectedFirst(),
            bleDevices = bleDevices.withConnectedFirst(),
            classicScanState = classicScanState,
            bleScanState = bleScanState,
            connectedDevice = connectedDevice,
            connectionState = connectionState,
        )
    }

    private fun emitNavigationEvent(event: BluetoothDevicesNavigation) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
