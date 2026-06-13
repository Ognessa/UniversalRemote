# Universal Remote

A Kotlin Multiplatform app for Android and iOS that lets users design custom Bluetooth remote controllers. You build a controller layout with buttons and sliders, assign signals to each element, then connect to a Bluetooth device and use the controller in the playground.

## Tech stack

| Area | Library / Tool |
|---|---|
| Language | Kotlin 2.3 |
| UI | Compose Multiplatform 1.10 |
| Platforms | Android (minSdk 24), iOS (arm64, simulatorArm64) |
| DI | Koin 4.2 |
| Navigation | Jetbrains Navigation Compose 2.9 |
| Database | SQLDelight 2.3 |
| Serialization | kotlinx.serialization |
| Bluetooth | Platform APIs via expect/actual in `:feature-bluetooth-manager` |
| Testing | kotlin.test, Mokkery, kotlinx-coroutines-test |
| Linting | ktlint 1.2, detekt 1.23 |
| Crash reporting | Firebase Crashlytics |
| Distribution | Firebase App Distribution |
| CI | GitHub Actions |

## Modules

| Module | Responsibility | Depends on |
|---|---|---|
| `:androidApp` | Android entry point, Activity, Koin/Firebase init | `:composeApp`, `:feature-controller`, `:feature-bluetooth-manager` |
| `:composeApp` | KMP shared app layer, navigation graph, DI wiring | all feature modules, `:core` |
| `:core` | Shared UI primitives, navigation abstractions, design tokens | — |
| `:feature-controller` | Domain models, SQLDelight DB, `ControllerRepository`, canvas rendering | `:core` |
| `:feature-list-api` | `ControlsListScreenBuilder` contract | `:core` |
| `:feature-list-presentation` | Screen: list of saved controllers | `:core`, `:feature-controller`, `:feature-list-api` |
| `:feature-editor-api` | `ControllerEditorScreenBuilder` + `EditorSharedState` contracts | `:core` |
| `:feature-editor-presentation` | Screen: drag-and-drop canvas editor for building a controller layout | `:core`, `:feature-controller`, `:feature-editor-api`, `:feature-library-api`, `:feature-signal-api`, `:feature-title-api` |
| `:feature-library-api` | `EditorLibraryScreenBuilder` contract | `:core` |
| `:feature-library-presentation` | Screen: element library picker (buttons, sliders, etc.) | `:core`, `:feature-controller`, `:feature-library-api`, `:feature-editor-api` |
| `:feature-signal-api` | `SignalEditorScreenBuilder` contract | `:core` |
| `:feature-signal-presentation` | Screen: configure the signal emitted by a controller element | `:core`, `:feature-controller`, `:feature-signal-api` |
| `:feature-title-api` | `TitleEditorDialogBuilder` contract | `:core` |
| `:feature-title-presentation` | Dialog: rename a controller | `:core`, `:feature-controller`, `:feature-title-api` |
| `:feature-playground-api` | `PlaygroundScreenBuilder` contract | `:core` |
| `:feature-playground-presentation` | Screen: run a controller and send signals over Bluetooth | `:core`, `:feature-controller`, `:feature-playground-api`, `:feature-bluetooth-api`, `:feature-bluetooth-manager` |
| `:feature-bluetooth-api` | `DevicePickerScreenBuilder` contract | `:core` |
| `:feature-bluetooth-presentation` | Screen: scan and pick a Bluetooth device | `:core`, `:feature-bluetooth-api` |
| `:feature-bluetooth-manager` | Platform Bluetooth scanning and connection (expect/actual) | Koin core |

## Build

### Prerequisites

- JDK 17
- Android SDK (compileSdk 37)
- `secrets.properties` in the project root (see CI workflow for required keys)
- `composeApp/src/google-services.json` (Firebase Android config)
- `keystore/release_key.jks` + `keystore/keystore.properties` for release signing

### Android

```shell
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and run the `iosApp` scheme, or build from the command line:

```shell
xcodebuild \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'generic/platform=iOS Simulator' \
  build
```

Xcode drives the Gradle build for the shared KMP framework automatically.

## Tests

```shell
# Common + Android unit tests
./gradlew test

# iOS tests (requires Apple Silicon or an arm64 simulator)
./gradlew iosSimulatorArm64Test
```

### Linting

```shell
./gradlew ktlintCheck detekt
```

## CI

GitHub Actions runs on push/PR to `main` and `develop`:

- **Android job** (`ubuntu-latest`): ktlint + detekt + tests → debug APK → Firebase App Distribution upload
- **iOS job** (`macos-latest`): iOS simulator tests → Xcode debug build