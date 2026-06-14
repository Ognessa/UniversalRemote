# CLAUDE.md

## Project overview

Kotlin Multiplatform app targeting **Android** (minSdk 24, compileSdk 37) and **iOS** (iosArm64, iosSimulatorArm64).

The app lets users design custom Bluetooth remote controllers — build a layout of buttons and sliders, assign signals to each element, connect to a Bluetooth device via Classic or BLE, then operate the controller in a live playground screen.

Package root: `com.patorika`

---

## Module structure

Gradle modules are defined in `settings.gradle.kts`. Physical paths are in `feature/<domain>/<layer>`.

| Module | Path | Responsibility |
|---|---|---|
| `:androidApp` | `androidApp/` | Android `Application` + `MainActivity`; starts Koin with platform modules |
| `:composeApp` | `composeApp/` | KMP shared root: `App()` composable, `NavHost`, `appModule` Koin aggregator |
| `:core` | `core/` | `ScreenBuilder`, `AppNotificationManager`, `TextProvider`, shared UI primitives |
| `:feature-controller` | `feature/controller/` | Domain + data: `ControllerRepository`, SQLDelight DB, element models, canvas rendering, serialization |
| `:feature-list-api` | `feature/list/api/` | `ControlsListScreenBuilder` interface |
| `:feature-list-presentation` | `feature/list/presentation/` | Controller list screen; CRUD use cases |
| `:feature-editor-api` | `feature/editor/api/` | `ControllerEditorScreenBuilder`, `EditorSharedState` interface |
| `:feature-editor-presentation` | `feature/editor/presentation/` | Drag-and-drop canvas editor screen |
| `:feature-library-api` | `feature/library/api/` | `EditorLibraryScreenBuilder` interface |
| `:feature-library-presentation` | `feature/library/presentation/` | Element library picker screen |
| `:feature-signal-api` | `feature/signal/api/` | `SignalEditorScreenBuilder` interface |
| `:feature-signal-presentation` | `feature/signal/presentation/` | Signal configuration screen for a single element |
| `:feature-title-api` | `feature/title/api/` | `TitleEditorDialogBuilder`, `TitleEditorNavArgs` |
| `:feature-title-presentation` | `feature/title/presentation/` | Rename-controller dialog |
| `:feature-playground-api` | `feature/playground/api/` | `PlaygroundScreenBuilder` interface |
| `:feature-playground-presentation` | `feature/playground/presentation/` | Live controller screen; sends signals over Bluetooth |
| `:feature-bluetooth-api` | `feature/bluetooth/api/` | `DevicePickerScreenBuilder` interface |
| `:feature-bluetooth-presentation` | `feature/bluetooth/presentation/` | Bluetooth device picker screen (Classic + BLE tabs) |
| `:feature-bluetooth-manager` | `feature/bluetooth/manager/` | Platform Bluetooth scanning and connection; no Compose dependency |

---

## Architecture

### Layer model

```
:androidApp / iosApp (Swift)
    └── :composeApp          ← NavHost, appModule, App()
         ├── :core           ← shared UI, ScreenBuilder contract, notifications
         ├── :feature-controller  ← domain + data (SQLDelight, serialization)
         └── :feature-*-api + :feature-*-presentation  ← feature slices
```

### Feature module convention

Each feature is split into two modules:

- **`*-api`** — contains only the `ScreenBuilder` interface (or `DialogBuilder`) and navigation arg types. No Compose dependencies on sibling features.
- **`*-presentation`** — contains the full slice: ViewModel, Screen composable, use cases, DI module, `ScreenBuilderImpl`.

`:feature-controller` is the exception: it is a single module combining data/domain/presentation models and shared canvas UI because all features reference its models.

### ViewModel contract

Every ViewModel:

```kotlin
class XyzViewModel(...) : ViewModel() {
    private val _state = MutableStateFlow(XyzScreenState())
    val state: StateFlow<XyzScreenState> = _state

    private val _events = MutableSharedFlow<XyzNavigation>()
    val events = _events.asSharedFlow()

    fun onEvent(event: XyzEvents) { ... }
}
```

- UI state: `StateFlow<ScreenState>` (data class with defaults)
- Navigation: `SharedFlow<NavigationEvent>` (sealed interface), collected in the Screen via `repeatOnLifecycle(STARTED)`
- User input: `onEvent(SealedEvents)` — single entry point

### Navigation

`ScreenBuilder` (defined in `:core`):

```kotlin
interface ScreenBuilder {
    val routeName: String
    fun build(builder: NavGraphBuilder, navController: NavController)
}
```

Every presentation module registers its `ScreenBuilderImpl` in Koin with `bind ScreenBuilder::class`. `App.kt` collects all registered instances via `getKoin().getAll<ScreenBuilder>()` and registers them in a single `NavHost`. The start destination is `ControlsListScreenBuilder.routeName`.

Route naming: plain `String` constants (e.g. `"SignalEditor"`, `"ControlsList"`). Path-segment nav args: `navController.navigate("${route}/${id}")`. Complex objects are Base64-URL-encoded JSON strings (see `ControllerElementModel.toNavArg()`).

### Use cases

Live in `feature/*-presentation/src/commonMain/.../usecase/`. Single `execute()` method, no base class. They are thin wrappers around the repository and are tested via the ViewModel test (real use case + mocked repository).

### Notifications

`AppNotificationManager` (`:core`) is a `SharedFlow`-backed singleton. ViewModels post `AppNotification.SnackBar` or `AppNotification.Dialog`; `App.kt` observes and renders them globally. Use `TextProvider.Res(Res.string.xyz)` for localised strings, `TextProvider.Text(str)` for dynamic strings.

---

## Tech stack

| Concern | Library | Version |
|---|---|---|
| Kotlin / KMP | `kotlin-multiplatform` | 2.3.21 |
| UI | Compose Multiplatform | 1.10.3 |
| UI components | Material3 | 1.9.0 |
| DI | Koin | 4.2.1 |
| Navigation | `navigation-compose` (Jetbrains) | 2.9.2 |
| Database | SQLDelight | 2.3.2 |
| Serialization | `kotlinx-serialization-json` | 1.11.0 |
| Coroutines | `kotlinx-coroutines-core` | 1.10.2 |
| Logging | Kermit (`co.touchlab:kermit`) | 2.1.0 |
| Mocking (tests) | Mokkery | 3.3.0 |
| Linting | ktlint 1.2.1, detekt 1.23.8 | — |
| Firebase | BOM 34.13.0 (Analytics, Crashlytics, AppDistribution) | — |
| Build | AGP 9.2.1, JDK 17 | — |

---

## DI (Koin) — how it's wired

### Common modules

Each module defines a top-level `val`:

```kotlin
// feature/list/presentation/di/ControlsListModule.kt
val controlsListModule = module { ... }
```

Aggregated in `:composeApp`:

```kotlin
// composeApp/di/AppModule.kt
val appModule = module {
    includes(
        coreModule, controllerModule, controlsListModule,
        controllerEditorModule, editorLibraryModule, signalEditorModule,
        titleEditorModule, playgroundModule, bluetoothManagerModule, bluetoothDevicesModule,
    )
}
```

### Platform-specific modules

`DatabaseDriverFactory` and `BluetoothManagerFactory` require platform context. They are provided by separate platform modules and must be loaded **before** `appModule`:

| Module val | Source set | Provides |
|---|---|---|
| `androidControllerModule` | `feature/controller/androidMain` | `DatabaseDriverFactory(context)` |
| `iosControllerModule` | `feature/controller/iosMain` | `DatabaseDriverFactory()` |
| `androidBluetoothModule` | `feature/bluetooth/manager/androidMain` | `BluetoothManagerFactory(context)` |
| `iosBluetoothModule` | `feature/bluetooth/manager/iosMain` | `BluetoothManagerFactory()` |

**Android** (`AndroidApplication.kt`):
```kotlin
startKoin { androidContext(this); modules(androidControllerModule, androidBluetoothModule, appModule) }
```

**iOS** (`AppDelegate.kt`):
```kotlin
fun initKoinIos() { startKoin { modules(iosControllerModule, iosBluetoothModule, appModule) } }
```
`initKoinIos()` is called from Swift before the Compose window is created.

### Koin patterns used

- `single<Interface> { Impl() }` — singletons (Repository, BluetoothManager, AppNotificationManager)
- `factory<Interface> { Impl() } bind ScreenBuilder::class` — ScreenBuilders
- `viewModel { XyzViewModel(get(), get()) }` — ViewModels (do **not** use `koinViewModel()` outside Compose)
- `onClose { it?.close() }` — used on `BluetoothManager` to release platform resources

---

## expect/actual

All `expect` declarations and their source-set layout:

| Symbol | commonMain | androidMain | iosMain |
|---|---|---|---|
| `expect fun getPlatform(): Platform` | `core/util/Platform.kt` | `Platform.android.kt` | `Platform.ios.kt` |
| `expect class DatabaseDriverFactory` | `feature/controller/data/database/DatabaseDriverFactory.kt` | ← Android JDBC | ← iOS native |
| `expect class BluetoothManagerFactory` | `feature/bluetooth/manager/data/BluetoothManagerFactory.kt` | ← Android impl | ← iOS CoreBluetooth impl |

Test-only:

| Symbol | commonTest | androidUnitTest | iosTest |
|---|---|---|---|
| `expect fun createTestDatabaseDriver(): SqlDriver` | `FakeDBDriver.kt` | JdbcSqliteDriver in-memory | native in-memory |

---

## Build & run

### Prerequisites

- JDK 17
- `secrets.properties` in project root (required by `GenerateSecretsTask`)
- `composeApp/src/google-services.json` (Firebase Android config)
- `keystore/release_key.jks` + `keystore/keystore.properties` (release signing)

### Gradle tasks

```shell
# Android debug APK
./gradlew assembleDebug

# Android release APK
./gradlew assembleRelease

# Lint + static analysis
./gradlew ktlintCheck detekt

# Fix ktlint violations
./gradlew ktlintFormat

# All unit tests (Android + common)
./gradlew test

# iOS tests (arm64 simulator)
./gradlew iosSimulatorArm64Test

# Upload Android debug to Firebase App Distribution
./gradlew appDistributionUploadDebug
```

### iOS build

```shell
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'generic/platform=iOS Simulator' \
  build
```

The Xcode build drives Gradle for the shared framework automatically.

### Code generation tasks (run automatically before compile)

Defined in `buildSrc/src/main/kotlin/tasks/`:

- `generateSecretsClass` — reads `secrets.properties`, writes `composeApp/src/commonMain/.../Secrets.kt`
- `generateBuildPropsClass` — writes `BuildProps.kt` with `versionCode`/`versionName` from `libs.versions.toml`
- `updatePlistVersion` — syncs `iosApp/iosApp/Info.plist` version fields

These are wired as dependencies of `generateComposeResClass` in `composeApp/build.gradle.kts`.

---

## Testing

### Frameworks

| Source set | Frameworks |
|---|---|
| `commonTest` | `kotlin.test`, `kotlinx-coroutines-test`, Mokkery |
| `androidUnitTest` | same (+ JdbcSqliteDriver for DB tests) |
| `iosTest` | same (+ native SQLDelight driver for DB tests) |

**Do not use MockK.** It is not KMP-compatible. The project uses **Mokkery** for all mocking.

### Mokkery usage

```kotlin
import dev.mokkery.mock
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.answering.returns
import dev.mokkery.matcher.any

val repo = mock<ControllerRepository>()
every { repo.getAllControllers() } returns flowOf(Result.success(listOf(...)))
everySuspend { repo.insertController(any()) } returns Result.success(Unit)
```

### ViewModel tests

- Mock the repository interface with Mokkery.
- Use **real use cases** (they are thin wrappers; mocking them adds no value).
- Set main dispatcher in `@BeforeTest`, reset in `@AfterTest`:

```kotlin
@BeforeTest fun setup() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
}
@AfterTest fun teardown() {
    Dispatchers.resetMain()
}
```

- Use `runTest { ... }` + `yield()` to advance coroutines past `launch` boundaries.
- Collect navigation events with `async { viewModel.events.collect { ... } }`, then `yield()`, then trigger the event, then `yield()`, then `collector.cancel()`.

### Repository tests

Use a real in-memory SQLite database via `createTestDatabaseDriver()` — **not mocked**. This is intentional to catch SQL logic bugs. `@BeforeTest` creates the driver; `@AfterTest` calls `driver.close()`.

### Test naming

Backtick style: `` `initial load success updates controllersList and stops loading` ``

### Where test files live

Mirror the production package structure under `src/commonTest/kotlin/`.

---

## Code conventions

### Naming

- Gradle module names: kebab-case with `-` (`:feature-list-presentation`)
- Kotlin package names: snake_case with `_` (`com.patorika.feature_list_presentation`)
- ViewModel: `XyzViewModel`
- Screen composable: `XyzScreen`
- UI state: `XyzScreenState` (data class)
- User events: `XyzEvents` or `XyzUserEvent` (sealed interface)
- Navigation events: `XyzNavigation` (sealed interface)
- Koin module value: `xyzModule`

### File layout inside a presentation module

```
src/commonMain/kotlin/com/patorika/<module>/
    api/          ← ScreenBuilderImpl
    di/           ← single Koin module val
    model/        ← ScreenState, Events, Navigation sealed types
    ui/           ← Screen.kt, ViewModel.kt
    ui/components/← sub-composables
    usecase/      ← use case classes
```

### Screens

Screen composables receive a ViewModel and a `navigate: (Navigation) -> Unit` lambda. They do **not** call `koinViewModel()` internally — the DI injection happens in `ScreenBuilderImpl.build()`:

```kotlin
builder.composable(routeName) {
    XyzScreen(viewModel = koinViewModel(), navigate = { handleNavigation(navController, it) })
}
```

### Serialization of controller elements

`ControllerElementModel` is a `@Polymorphic` sealed hierarchy. Every new concrete element type must be:
1. Annotated `@Serializable @SerialName("TypeName")`
2. Added as `subclass(NewElementModel::class)` in the `controllerModelModule` in `ControllerElementSerialization.kt`

The `classDiscriminator` is `"type"`. The JSON `type` field must match the `@SerialName`.

### Compose resources

String resources live in `src/commonMain/composeResources/values/strings.xml` of the relevant module. Access via generated `Res.string.*`. Pass to `AppNotificationManager` wrapped in `TextProvider.Res(Res.string.xyz)`.

---

## What NOT to do

- **Don't use MockK** — not KMP-compatible; use Mokkery.
- **Don't use `@AndroidEntryPoint` or Hilt** — DI is Koin throughout.
- **Don't inject `android.content.Context` in `commonMain`** — provide it through a platform-specific Koin module (`androidMain`).
- **Don't add new features to `App.kt` or `appModule` manually** — add the feature's Koin module to `appModule.includes()` and bind its `ScreenBuilder` with `bind ScreenBuilder::class`; `App.kt` auto-discovers all registered `ScreenBuilder` instances.
- **Don't load `appModule` without the platform modules first** — `DatabaseDriverFactory` and `BluetoothManagerFactory` are platform-specific `expect` classes and must be provided before `appModule` starts.
- **Don't create a new element type without updating `controllerModelModule`** — the polymorphic serializer will throw at runtime without the `subclass(...)` registration.
- **Don't call `Dispatchers.Main` directly in production `commonMain` code without test setup** — ViewModel tests must call `Dispatchers.setMain(UnconfinedTestDispatcher())` first.
- **Don't add MockK, Hilt, Room, or Retrofit** — none of these are present or intended; the stack is Mokkery + Koin + SQLDelight + platform Bluetooth APIs.
- **Don't change the SQLDelight schema without adding a versioned migration file** — the schema is at version 1 with no `.sqm` files; any schema change requires a numbered migration or existing user data will be lost on upgrade.
- **Don't persist element changes made in the playground** — `ElementModified` events update in-memory state only; element modifications during play are intentionally ephemeral (e.g. slider positions) and must not be written back to the database.