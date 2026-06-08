# :feature-bluetooth-presentation

Bluetooth device picker: scans for Classic and BLE devices, shows paired devices, and manages connect/disconnect.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `DevicePickerViewModel` | Directly holds `BluetoothManager`. Combines six `StateFlow`s from the manager into a single `DevicePickerUiState` via nested `combine` calls, published as a `stateIn(WhileSubscribed(5000))`. On `Close` stops both scans, clears discovered devices, emits `BluetoothDevicesNavigation.Close`. `onCleared()` also calls `cleanup()`. |
| `DevicePickerUiState` | `data class`; fields: `isBluetoothEnabled`, `devices: DeviceListState`, `scanState: ScanStateModel`, `connectionState: ConnectionState`. `checkDeviceConnectionState(device)` returns the connection state only for the currently connected device (idle for all others). |
| `DeviceListState` | `data class`; `paired`, `discoveredClassic`, `ble` lists of `BluetoothDevice`. In `buildUiState` the connected device is sorted to the top of its list. |
| `ScanStateModel` | `data class`; combines `classic: ScanState` and `ble: ScanState`. |
| `ConnectionState` | `data class`; `device: BluetoothDevice?` and `state: DeviceConnectionState`. |
| `DevicePickerEvents` | Sealed interface: `Close`, `StartClassicScan`, `StartBleScan`, `ConnectToDevice(device)`, `Disconnect`. |
| `DevicePickerScreenBuilderImpl` | Registers the route; pops back stack on `Close`. |

The screen has two tabs: Classic Bluetooth (paired + discovered) and BLE. Permission and Bluetooth-disabled states each have a dedicated composable (`BluetoothPermissionDeniedContent`, `BluetoothDisabledContent`).

Platform-specific files:
- `BluetoothPermissionRequest` (androidMain / iosMain) — requests runtime Bluetooth permissions.
- `AppSettings` (androidMain / iosMain) — opens system settings to the app's permission page.

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` |
| `:feature-bluetooth-api` | `DevicePickerScreenBuilder` contract |
| `:feature-bluetooth-manager` | `BluetoothManager`, `BluetoothDevice`, `ScanState`, `DeviceConnectionState` |

## Testing

No tests in this module.