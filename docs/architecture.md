# Architecture

## Overview

Universal Remote is a Kotlin Multiplatform app targeting **Android** (minSdk 24, compileSdk 37) and **iOS** (iosArm64, iosSimulatorArm64). It lets users design custom Bluetooth remote controllers: build a canvas layout of buttons and sliders, assign signal strings to each element, connect to a Bluetooth device (Classic or BLE), then operate the controller live in a playground screen.

The shared UI layer is Compose Multiplatform. All business logic, data access, and navigation live in shared `commonMain` source sets. Platform-specific code is limited to `DatabaseDriverFactory` and `BluetoothManagerFactory`, both provided through Koin platform modules.

---

## Layer diagram

```
┌───────────────────────────────────────────────────────┐
│  Platform shells                                      │
│  :androidApp  (Application, MainActivity)             │
│  iosApp/      (iOSApp.swift → ComposeUIViewController)│
└────────────────────────┬──────────────────────────────┘
                         │
┌────────────────────────▼──────────────────────────────┐
│  App shell  :composeApp                               │
│  App(), NavHost, appModule, code-gen Gradle tasks     │
└──┬──────────┬───────────────────────────────────┬─────┘
   │          │                                   │
   ▼          ▼                                   ▼
:core    :feature-*-presentation           :feature-*-api
         (ViewModel, Screen,               (ScreenBuilder /
          UseCase, DI module,              DialogBuilder interface,
          ScreenBuilderImpl)               nav arg types)
   │          │
   │          ▼
   │    :feature-controller
   │    (domain interface + SQLDelight
   │     data layer + shared element
   │     models + ControllerCanvas)
   │          │
   └──────────┘
              │
              ▼
   :feature-bluetooth-manager
   (BluetoothManager interface + platform
    impls, no Compose dependency)
```

**Layers and what lives in each:**

| Layer | Modules | Contents |
|---|---|---|
| Platform shell | `:androidApp`, `iosApp/` | Entry point only; starts Koin with platform modules, launches `App()` |
| App shell | `:composeApp` | `NavHost`, global notification UI, `appModule` DI aggregator, code-generation Gradle tasks |
| Core | `:core` | `ScreenBuilder` nav contract, `AppNotificationManager`, `TextProvider`, shared UI primitives, `Platform` expect |
| Feature API | `:feature-*-api` | `ScreenBuilder`/`DialogBuilder` interface + nav arg types — no Compose, no sibling-feature dependencies |
| Feature Presentation | `:feature-*-presentation` | ViewModel, Screen composable, use cases, Koin module, `ScreenBuilderImpl` |
| Domain + Data | `:feature-controller` | `ControllerRepository` interface, `ControllerRepositoryImpl`, SQLDelight `Database`, all `ControllerElementModel` types, `ControllerCanvas` composable |
| Infrastructure | `:feature-bluetooth-manager` | `BluetoothManager` interface, Android (BroadcastReceiver + BluetoothGatt) and iOS (CoreBluetooth) impls; no Compose dependency |

---

## Module dependency graph

Arrows point from dependent → dependency.

```
:androidApp ──────────────────────────────────────────────────────────┐
    ├─► :composeApp                                                    │
    ├─► :feature-controller (androidControllerModule)                  │
    └─► :feature-bluetooth-manager (androidBluetoothModule)            │
                                                                       │
:composeApp ──────────────────────────────────────────────────────────►│
    ├─► :core                                                          │
    ├─► :feature-controller                                            │
    ├─► :feature-list-api + :feature-list-presentation                 │
    ├─► :feature-editor-api + :feature-editor-presentation             │
    ├─► :feature-library-api + :feature-library-presentation           │
    ├─► :feature-signal-api + :feature-signal-presentation             │
    ├─► :feature-title-api + :feature-title-presentation               │
    ├─► :feature-playground-api + :feature-playground-presentation     │
    ├─► :feature-bluetooth-api + :feature-bluetooth-presentation       │
    └─► :feature-bluetooth-manager                                     │
                                                                       │
:feature-*-presentation ──────────────────────────────────────────────┘
    ├─► :core
    ├─► :feature-controller          (all presentation modules)
    ├─► :feature-*-api               (own api module)
    └─► other :feature-*-api         (for cross-feature navigation only)

:feature-playground-presentation
    └─► :feature-bluetooth-manager   (only presentation that touches manager directly)

:feature-bluetooth-manager
    └─► (no project module dependencies — intentionally isolated)

:feature-*-api
    └─► :core                        (ScreenBuilder interface)
```

**Cross-feature navigation is always via API modules only.** Presentation modules never import sibling presentation modules.

---

## Key architectural decisions

### MVVM with unidirectional data flow

Every screen follows a strict contract:

```
UI events ──► ViewModel.onEvent(SealedEvent)
                  │
                  ├── updates ──► MutableStateFlow<ScreenState>  ──► UI renders
                  └── emits  ──► MutableSharedFlow<NavEvent>     ──► collected in Screen
```

- **UI state** is a `StateFlow<XyzScreenState>` (data class, all defaults, never null fields).
- **Navigation** is a `SharedFlow<XyzNavigation>` (sealed interface), collected with `repeatOnLifecycle(STARTED)` so events are not replayed after the lifecycle drops below STARTED.
- **User input** enters through a single `onEvent(SealedEvents)` method — no individual public mutators.

### Feature API/presentation split

Each feature is two Gradle modules:

- `*-api` — interface + nav arg types only. Other features depend only on the API module to navigate into a feature without pulling in its full implementation.
- `*-presentation` — full implementation. Depends on its own API and on the API modules of any feature it can navigate to.

This makes the dependency graph acyclic and keeps compile scope minimal.

### Koin-driven auto-discovery of screens

`ScreenBuilderImpl` in every presentation module is registered in Koin with `bind ScreenBuilder::class`. `App.kt` collects all registered instances via `getKoin().getAll<ScreenBuilder>()` and calls `build()` on each inside the single `NavHost`. New screens are wired by adding one module to `appModule.includes()` — no changes to `App.kt`.

### Result-wrapped repository API

`ControllerRepository` wraps every return in Kotlin's `Result<T>`. `Flow<Result<List<T>>>` is used for reactive queries; `suspend fun … : Result<T>` for one-shot writes. ViewModels never handle raw exceptions — they call `.onSuccess` / `.onFailure` and post `AppNotification` for user feedback.

### Global notification bus

`AppNotificationManager` is a zero-replay `SharedFlow` singleton. ViewModels post `AppNotification.SnackBar` or `AppNotification.Dialog`; `App.kt` observes and renders them in a single `Scaffold`. Features never own their own snackbar infrastructure.

### Polymorphic element serialization

`ControllerElementModel` is a `@Polymorphic` sealed hierarchy. Elements are stored in SQLDelight as JSON strings using a custom `controllerJson` instance with `classDiscriminator = "type"`. The same JSON is used for navigation args (Base64-URL-encoded). Every new element type must be registered in `controllerModelModule` in `ControllerElementSerialization.kt`.

---

## Data flow

A typical read flow (controller list screen):

```
SQLDelight ControllerDatabase
    │  asFlow().mapToList(Dispatchers.IO)
    ▼
Database.getAllControllers(): Flow<List<ControllerModel>>
    │  map { Result.success(it) } / catch { Result.failure(it) }
    ▼
ControllerRepositoryImpl.getAllControllers(): Flow<Result<List<ControllerModel>>>
    │
    ▼
GetAllControllersUseCase.execute()          (thin wrapper, no logic)
    │
    ▼
ControlsListViewModel
    │  collectLatest in viewModelScope
    │  onSuccess → _state.update { copy(controllersList = list) }
    │  onFailure → appNotificationManager.send(SnackBar)
    ▼
StateFlow<ControlsListScreenState>
    │  collectAsStateWithLifecycle() in composable
    ▼
ControlsListScreen (UI renders)
```

A write flow (delete controller):

```
User taps delete
    ▼
ControlsListScreen calls viewModel.onEvent(Item.Delete(id))
    ▼
ControlsListViewModel.onDelete(id)
    │  viewModelScope.launch
    ▼
DeleteControllerUseCase.execute(id)
    ▼
ControllerRepositoryImpl.removeController(id)
    │  withContext(Dispatchers.IO)
    ▼
Database.removeController(id)        (SQL DELETE, cascade to elements)
    │
    └── SQLDelight reactive query re-emits → UI updates automatically
    └── Result.success → appNotificationManager.send(SnackBar "deleted")
```
