# :core

Shared foundation module providing navigation contracts, a global notification bus, UI utilities, and platform abstractions used by every other module.

**Layer:** Core / infrastructure

## Key classes

| Class / Interface | What it does |
|---|---|
| `ScreenBuilder` | Navigation contract interface. Every feature's presentation module implements this to register itself with the `NavHost`. Declares `routeName: String` and `fun build(NavGraphBuilder, NavController)`. |
| `AppNotificationManager` / `AppNotificationManagerImpl` | `SharedFlow`-backed singleton bus. ViewModels call `send(AppNotification)` to post a snackbar or dialog; `App()` observes and renders globally. Zero-replay — late subscribers do not receive past notifications. |
| `AppNotification` | Sealed class with two subtypes: `SnackBar` (message + optional action label + callback) and `Dialog` (title, message, confirm/dismiss labels + callbacks). |
| `TextProvider` | Sealed class wrapping either a raw `String` (`TextProvider.Text`) or a Compose `StringResource` (`TextProvider.Res`). Used throughout to pass localised or dynamic strings in a platform-neutral way. |
| `LoggerUtil` | `expect object` for logging. `isEnabled` flag lets tests suppress output. Actuals: Android uses `android.util.Log`, iOS uses `NSLog`. |

Other notable contents:
- `Platform` — `expect fun getPlatform(): Platform` returning `ANDROID` or `IOS`.
- `CoreDimens` — shared spacing/size constants.
- `CustomLoader` — shared loading indicator composable.
- `CustomDropdownMenu` / `CustomDropdownItemModel` — shared dropdown menu composable.
- `DialogAnimation` — shared enter/exit transition for dialogs.
- `MeasurementsExt` — `pxToDp()` extension.

## Dependencies

No project module dependencies.

## External dependencies

| Library | Why |
|---|---|
| Compose Multiplatform (Material3, Foundation, Navigation) | UI primitives and the `NavGraphBuilder`/`NavController` types used in `ScreenBuilder` |
| Koin Core + Compose | DI; `coreModule` registers `AppNotificationManager` as a singleton |
| `navigation-compose` (Jetbrains) | `NavGraphBuilder` / `NavController` in `ScreenBuilder` |

## Testing

`AppNotificationManagerImplTest` in `commonTest` covers:
- Single notification delivery to one collector
- Broadcast to multiple concurrent collectors
- Zero-replay guarantee (late subscriber receives nothing)
- Emission order preservation across multiple sequential sends

Uses `kotlin.test`, `kotlinx-coroutines-test`, `runTest` + `yield()` + `async { flow.first() }` pattern.