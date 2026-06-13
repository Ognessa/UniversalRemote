# ADR 002 — Mokkery over MockK for KMP mocking

**Status:** Accepted

## Context

The project uses Kotlin Multiplatform with test logic in `commonTest`, which compiles and runs on both the JVM (Android host tests) and Kotlin/Native (iOS simulator tests). A mocking library is needed to isolate `ControllerRepository` from ViewModels and use cases in tests.

MockK is the dominant mocking library in the Android/Kotlin ecosystem, but it relies on JVM reflection and the Java `Proxy` API. Neither is available on Kotlin/Native. MockK cannot run in `commonTest` or `iosTest` source sets, making it incompatible with the project's KMP test strategy.

## Decision

**Mokkery** (`dev.mokkery`, version 3.3.0) is used for all mocking. Mokkery is a Gradle compiler plugin that generates mock implementations at compile time, producing code that is fully compatible with Kotlin/Native and Kotlin/JS in addition to the JVM.

Mokkery is applied as a Gradle plugin in each module that requires mocking:

```kotlin
plugins {
    alias(libs.plugins.mokkery)
}
```

Usage in tests:

```kotlin
val repo = mock<ControllerRepository>()
every { repo.getAllControllers() } returns flowOf(Result.success(emptyList()))
everySuspend { repo.insertController(any()) } returns Result.success(Unit)
verifySuspend { repo.insertController(matches { it.id != original.id }) }
```

## Consequences

**Enables:**
- All mocking logic lives in `commonTest` and runs identically on Android host tests and iOS simulator tests without any platform-specific test code.
- Compile-time mock generation catches API mismatches at build time rather than at runtime.

**Constrains:**
- MockK must not be added to the project — it will not work in `commonTest` or `iosTest`.
- Mokkery requires the Gradle plugin to be applied to each module that uses mocking; it cannot be applied globally.
- Mokkery can only mock interfaces and open classes — final production classes cannot be mocked.
