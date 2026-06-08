# :feature-playground-presentation

Live controller screen: renders a saved controller layout and sends element signals to the connected Bluetooth device.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `PlaygroundViewModel` | Loads the controller by ID via `GetControllerByIdUseCase`. Observes `BluetoothManager.isBluetoothEnabled`, `connectedDevice`, and `connectionState` — combined into `BluetoothState` via `combine`. On `ElementAction(action)` calls `bluetoothManager.sendSignal(action)`. On `ElementModified` updates the element within the in-memory controller state only — changes are intentionally ephemeral (e.g. slider positions during a play session) and are never written back to the database. On `OpenDevicePicker` / `Close` emits navigation events. |
| `PlaygroundScreenState` | `data class`; fields: `isLoading`, `controller: ControllerModel?`, `bluetoothState: BluetoothState`. |
| `BluetoothState` | `data class` snapshot: `isBluetoothEnabled`, `connectedDevice: BluetoothDevice?`, `connectionState: DeviceConnectionState`. |
| `PlaygroundUserEvent` | Sealed interface: `Close`, `OpenDevicePicker`, `ElementAction(action: String)`, `ElementModified(element)`. |
| `PlaygroundNavigationEvent` | Sealed interface: `Close`, `OpenDevicePicker`. |
| `GetControllerByIdUseCase` | Wraps `repository.getControllerById(id)`. |
| `PlaygroundScreenBuilderImpl` | Registers the route with a required `id` path segment. On `OpenDevicePicker` navigates to `DevicePickerScreenBuilder.routeName`. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` |
| `:feature-controller` | `ControllerRepository`, `ControllerModel`, `ControllerCanvas` (PLAY render mode) |
| `:feature-playground-api` | `PlaygroundScreenBuilder` contract |
| `:feature-bluetooth-api` | `DevicePickerScreenBuilder` (route for device picker navigation) |
| `:feature-bluetooth-manager` | `BluetoothManager` (observe state + `sendSignal`) |

## Testing

No tests in this module.