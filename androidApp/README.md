# :androidApp

Android application entry point that wires together the Koin DI graph and launches the shared Compose UI.

**Layer:** App shell (Android-only)

## Key classes

| Class | What it does |
|---|---|
| `AndroidApplication` | `Application` subclass; calls `startKoin { androidContext(...); modules(androidControllerModule, androidBluetoothModule, appModule) }` |
| `MainActivity` | Single `ComponentActivity`; calls `enableEdgeToEdge()` then `setContent { App() }` |

## Dependencies

| Module | Why |
|---|---|
| `:composeApp` | Provides `App()` composable and the shared `appModule` |
| `:feature-controller` | Provides `androidControllerModule` (`DatabaseDriverFactory` with `Context`) |
| `:feature-bluetooth-manager` | Provides `androidBluetoothModule` (`BluetoothManagerFactory` with `Context`) |

## External dependencies

| Library | Why |
|---|---|
| Firebase BOM + Analytics + Crashlytics | Crash reporting and analytics, configured here at the app level |
| Firebase App Distribution | Debug build distribution |
| Koin Android (`koin-android`, `koin-androidx-compose`) | Required to call `androidContext()` and use `koinInject()` from Compose |

## Testing

No unit tests. The placeholder `@Preview` in `MainActivity` exists for IDE preview purposes only.