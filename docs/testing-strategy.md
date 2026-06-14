# Testing Strategy

## Philosophy

Tests are written at the ViewModel and repository boundaries — the two places where logic actually lives. Use cases are thin wrappers around the repository; they are tested through the ViewModel rather than in isolation (except where the use case itself has observable behavior, such as `DuplicateControllerUseCase` which rekeys IDs). Platform glue (Activity, AppDelegate, Bluetooth hardware) is not tested.

The guiding rules:

- **Real use cases, mocked repositories** in ViewModel tests. Use cases contain no logic worth testing in isolation; mocking them adds no signal.
- **Real in-memory database** in repository tests. A mocked SQLDelight database would not catch SQL logic bugs. `ControllerRepositoryImpl` is tested directly against an in-memory SQLite driver.
- **No MockK** — it is not KMP-compatible. All mocking uses Mokkery.

---

## Test structure

| Source set | Module(s) | What lives there |
|---|---|---|
| `commonTest` | all modules with tests | All test logic — ViewModels, repository, serialization, use cases, notification manager |
| `androidUnitTest` | `:feature-controller` | `actual fun createTestDatabaseDriver()` — JVM in-memory SQLite driver |
| `iosTest` | `:feature-controller` | `actual fun createTestDatabaseDriver()` — native in-memory SQLite driver |

`commonTest` is the primary home for all tests. Platform-specific test source sets only exist to provide the `createTestDatabaseDriver` `expect`/`actual` implementation — no test logic lives there.

Test files mirror the production package structure under `src/commonTest/kotlin/`.

---

## Frameworks

| Source set | Library | Version | Purpose |
|---|---|---|---|
| `commonTest` | `kotlin.test` | 2.3.21 | Assertions (`assertEquals`, `assertTrue`, etc.), `@Test`, `@BeforeTest`, `@AfterTest` |
| `commonTest` | `kotlinx-coroutines-test` | 1.10.2 | `runTest`, `UnconfinedTestDispatcher`, `Dispatchers.setMain` / `resetMain`, `yield()` |
| `commonTest` | Mokkery | 3.3.0 | KMP-compatible mocking (`mock<T>()`, `every`, `everySuspend`, `verifySuspend`) |
| `androidUnitTest` | `sqldelight-sqlite-driver` (`JdbcSqliteDriver`) | 2.3.2 | In-memory SQLite for JVM-side DB tests |
| `iosTest` | `sqldelight-native-driver` (`inMemoryDriver`) | 2.3.2 | In-memory SQLite for iOS-side DB tests |

---

## Mocking approach

All mocking uses **Mokkery** (`dev.mokkery`, version 3.3.0), applied as a Gradle plugin in each presentation module's `build.gradle.kts` that requires mocking.

```kotlin
// Create a mock
val repo = mock<ControllerRepository>()

// Stub a Flow-returning function
every { repo.getAllControllers() } returns flowOf(Result.success(listOf(...)))

// Stub a suspend function
everySuspend { repo.insertController(any()) } returns Result.success(Unit)

// Verify a suspend call with a predicate
verifySuspend {
    repo.insertController(matches { it.id != original.id })
}
```

Mokkery is chosen over MockK because MockK relies on reflection that is not available on Kotlin/Native, making it incompatible with KMP `commonTest`. Mokkery generates mocks at compile time via a Gradle plugin, working identically on all targets.

`AppNotificationManager` is **not** mocked in ViewModel tests — the real `AppNotificationManagerImpl` is used so tests can assert that notifications are actually emitted.

`EditorSharedState` is mocked where needed (e.g. `SignalEditorViewModelTest`) because it carries cross-screen mutable state.

---

## Database testing

`ControllerRepository` is tested against a real in-memory SQLite database, not a mock. This catches SQL correctness issues (ordering, cascades, transactions) that a mock would silently pass.

The driver is provided via `expect`/`actual`:

```kotlin
// commonTest
expect fun createTestDatabaseDriver(): SqlDriver

// androidUnitTest
actual fun createTestDatabaseDriver(): SqlDriver {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    ControllerDatabase.Schema.create(driver)
    return driver
}

// iosTest
actual fun createTestDatabaseDriver(): SqlDriver =
    inMemoryDriver(ControllerDatabase.Schema)
```

Each test class manages the driver lifecycle:

```kotlin
@BeforeTest fun setup()   { driver = createTestDatabaseDriver() }
@AfterTest  fun teardown() { driver.close() }
```

---

## ViewModel coroutine setup

Every ViewModel test class follows the same lifecycle boilerplate:

```kotlin
@BeforeTest fun setup() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
}

@AfterTest fun teardown() {
    Dispatchers.resetMain()
}
```

`UnconfinedTestDispatcher` runs coroutines eagerly without suspension, so `viewModelScope.launch` blocks complete synchronously. `yield()` is used at boundaries to let coroutines started by the ViewModel's `init` block or `onEvent` calls complete before asserting state.

Navigation events are collected with a concurrent async collector:

```kotlin
var event: XyzNavigation? = null
val collector = async { viewModel.events.collect { event = it } }
yield()

viewModel.onEvent(SomeEvent)
yield()

collector.cancel()
assertEquals(expected, event)
```

---

## What is NOT tested

| Area | Reason |
|---|---|
| `BluetoothManager` (Android + iOS impls) | Requires real Bluetooth hardware and platform APIs |
| `DevicePickerViewModel` / `PlaygroundViewModel` | No tests written; live Bluetooth state is hard to fake in commonTest |
| `:composeApp` | Only a placeholder `1 + 2 == 3` test exists; app-shell logic is minimal |
| `:androidApp` | No tests; entry-point wiring is not unit-testable |
| UI rendering / Compose snapshots | No UI tests of any kind exist |
| iOS `BluetoothManagerImpl` (CoreBluetooth) | Requires a device or simulator with Bluetooth access |
| SQLDelight migration | Schema is at version 1; no migration files exist to test |
