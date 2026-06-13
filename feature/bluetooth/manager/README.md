# :feature-bluetooth-manager

Platform Bluetooth scanning and connection management. No Compose dependency — pure Kotlin + coroutines + platform APIs.

**Layer:** Infrastructure / platform service

## Key classes

| Class / Interface | What it does |
|---|---|
| `BluetoothManager` | Interface exposing eight `StateFlow` properties (`isBluetoothEnabled`, `pairedDevices`, `discoveredClassicDevices`, `bleDevices`, `classicScanState`, `bleScanState`, `connectedDevice`, `connectionState`) and actions: `loadPairedDevices()`, `startClassicScan()`, `stopClassicScan()`, `startBleScan()`, `stopBleScan()`, `connectToDevice(device)`, `disconnect()`, `sendSignal(signal: String)`, `clearDiscoveredDevices()`, `close()`. |
| `BluetoothManagerImpl` (androidMain) | Android implementation using `BluetoothAdapter`, a `BroadcastReceiver` for Classic scan results and state changes, `BluetoothLeScanner` for BLE, `BluetoothSocket` (SPP) for Classic connection, and `BluetoothGatt` for BLE connection. Uses a `SupervisorJob` coroutine scope; `close()` cancels the scope and unregisters the receiver. |
| `BluetoothManagerImpl` (iosMain) | iOS implementation using CoreBluetooth. BLE only — Classic Bluetooth is not exposed by CoreBluetooth, so Classic scan methods are no-ops and `classicScanState` is always `ScanState.Unavailable`. Uses two inner delegate objects (`CentralManagerDelegate` implementing `CBCentralManagerDelegateProtocol`, `PeripheralDelegate` implementing `CBPeripheralDelegateProtocol`) that bridge CoreBluetooth callbacks into lambdas which update `MutableStateFlow` properties — the same pattern as the Android `BroadcastReceiver`. The coroutine scope uses `Dispatchers.Main` because CoreBluetooth fires callbacks on the main thread. |
| `BluetoothManagerFactory` | `expect class`; Android actual takes `Context`, iOS actual takes no arguments. Creates a platform `BluetoothManagerImpl`. |
| `BluetoothDevice` | `data class`; `id: String`, `name: String?`, `type: DeviceType` (`CLASSIC` or `BLE`). |
| `DeviceConnectionState` | Sealed interface: `Idle`, `Connecting`, `Connected`, `Disconnecting`, `Reconnecting(attempt)`, `Error(message)`. |
| `ScanState` | Sealed interface: `Idle`, `Scanning(remainingSeconds)`, `Finished`, `Unavailable`, `Error(message)`. |

Scan durations are constants on the `BluetoothManager` companion: `CLASSIC_SCAN_DURATION = 12` seconds, `BLE_SCAN_DURATION = 10` seconds.

## Koin modules

| Module val | Source set | Provides |
|---|---|---|
| `bluetoothManagerModule` | `commonMain` | `BluetoothManager` as `single { factory.create() } onClose { it?.close() }` |
| `androidBluetoothModule` | `androidMain` | `BluetoothManagerFactory(context = androidContext())` |
| `iosBluetoothModule` | `iosMain` | `BluetoothManagerFactory()` |

`onClose` ensures `BluetoothManager.close()` is called when Koin stops, releasing the Android `BroadcastReceiver` and coroutine scope.

## Dependencies

No project module dependencies — intentionally isolated to avoid a Compose dependency in this infrastructure layer.

## External dependencies

| Library | Why |
|---|---|
| Koin Android (androidMain only) | `androidContext()` in `androidBluetoothModule` |

## Testing

No tests in this module.