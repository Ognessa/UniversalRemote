# ADR 003 — Platform database driver via expect/actual and Koin platform modules

**Status:** Accepted

## Context

SQLDelight requires a platform-specific `SqlDriver` to open a database:

- **Android** requires `AndroidSqliteDriver`, which takes an `android.content.Context` to locate the database file.
- **iOS** requires `NativeSqliteDriver`, which takes no context.

Both `commonMain` code and `commonTest` code need to obtain a driver, but `android.content.Context` cannot be referenced in `commonMain`. A strategy is needed to supply the correct driver on each platform without leaking platform types into shared code.

## Decision

Two separate mechanisms are used — one for production and one for tests.

**Production:** An `expect class DatabaseDriverFactory` is declared in `commonMain`. Each platform provides an `actual` class that constructs the appropriate `SqlDriver`:

- `androidMain`: `DatabaseDriverFactory(context: Context)` — uses `AndroidSqliteDriver`
- `iosMain`: `DatabaseDriverFactory()` — uses `NativeSqliteDriver`

`DatabaseDriverFactory` is provided to Koin through platform-specific modules that must be loaded before `appModule`:

```kotlin
// androidMain
val androidControllerModule = module {
    single { DatabaseDriverFactory(androidContext()) }
}

// iosMain
val iosControllerModule = module {
    single { DatabaseDriverFactory() }
}
```

`ControllerRepositoryImpl` receives the `SqlDriver` through Koin — it never references `DatabaseDriverFactory` directly.

**Tests:** An `expect fun createTestDatabaseDriver(): SqlDriver` is declared in `commonTest`. Each platform provides an `actual` that creates an in-memory driver:

- `androidUnitTest`: `JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)` + `Schema.create(driver)`
- `iosTest`: `inMemoryDriver(ControllerDatabase.Schema)`

Tests construct `ControllerRepositoryImpl(driver)` directly, bypassing Koin entirely.

## Consequences

**Enables:**
- Shared `commonMain` code never references any platform type; the `expect class` interface is the only boundary.
- Repository tests run against a real in-memory SQLite database on both JVM and Kotlin/Native without any conditional logic in the test code.
- Platform modules are small and explicit; it is obvious at the Koin startup call site which platform context is being injected.

**Constrains:**
- `appModule` must always be started after the platform module (`androidControllerModule` or `iosControllerModule`). Starting `appModule` first will cause a `NoBeanDefFoundException` for `DatabaseDriverFactory` at runtime.
- Adding a new platform target would require a new `actual` for both `DatabaseDriverFactory` and `createTestDatabaseDriver`.
