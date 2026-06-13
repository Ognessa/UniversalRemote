# :composeApp

KMP shared application layer that owns the navigation graph, global UI scaffolding, and the Koin module aggregator.

**Layer:** App shell (shared KMP)

## Key classes

| Class | What it does |
|---|---|
| `App` | Root `@Composable`; creates the `NavHost`, subscribes to `AppNotificationManager` and renders snackbars and dialogs globally. Auto-discovers all registered `ScreenBuilder` instances via `getKoin().getAll<ScreenBuilder>()` and registers them in one `NavHost`. Start destination is `ControlsListScreenBuilder.routeName`. |
| `appModule` | Koin `module { includes(...) }` that aggregates every feature's Koin module into one entry point for the shared app. |
| `MainViewController` | iOS entry point; returns a `ComposeUIViewController` wrapping `App()`. |
| `AppDelegate` (iosMain) | Exposes `initKoinIos()` — called from Swift before the window is created to start Koin with iOS platform modules. |
| `GenerateSecretsTask` / `GenerateBuildPropsTask` / `UpdatePlistVersionTask` | buildSrc Gradle tasks that generate `Secrets.kt`, `BuildProps.kt`, and sync `Info.plist` version at compile time. |

## Dependencies

`:composeApp` depends on all feature modules:

`:core`, `:feature-controller`, `:feature-list-api`, `:feature-list-presentation`, `:feature-editor-api`, `:feature-editor-presentation`, `:feature-library-api`, `:feature-library-presentation`, `:feature-signal-api`, `:feature-signal-presentation`, `:feature-title-api`, `:feature-title-presentation`, `:feature-playground-api`, `:feature-playground-presentation`, `:feature-bluetooth-api`, `:feature-bluetooth-presentation`, `:feature-bluetooth-manager`

## Testing

`ComposeAppCommonTest` in `commonTest` is a placeholder `1 + 2 == 3` test with no real coverage. There are no meaningful tests here; logic lives in feature modules.