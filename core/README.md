# :core

Shared foundation module providing navigation contracts, a global notification bus, UI utilities, and platform abstractions used by every other module.

**Layer:** Core / infrastructure

## Key classes

| Class / Interface | What it does |
|---|---|
| `ScreenBuilder` | Navigation contract interface. Every feature's presentation module implements this to register itself with the `NavHost`. Declares `routeName: String` and `fun build(NavGraphBuilder, NavController)`. |
| `NavDrawerScreenBuilder` | Contract for content rendered inside the navigation drawer rather than the `NavHost`. Declares `routeName: String` and a `@Composable fun Content(navController)`. Implemented by the General Menu. |
| `AppNavigationManager` / `AppNavigationManagerImpl` | `SharedFlow`-backed singleton bus for app-shell navigation and platform actions. ViewModels / screen builders call `send(AppNavigationEvent)`; `rememberAppNavigationState` in `:composeApp` collects and acts on them. Zero-replay. |
| `AppNavigationEvent` | Sealed class of app-shell intents: `OpenNavDrawer(routeName)`, `CloseNavDrawer`, `OpenBrowser(url)`, and `OpenEmail(recipient, subject?, body?)`. |
| `EmailLauncher` | Interface — `openEmail(recipient, subject?, body?): Boolean` — opens the device mail composer with the recipient (and optional subject/body) pre-filled. Returns `false` when no mail client can handle it so the caller can fall back. Platform impls: `AndroidEmailLauncher` (`ACTION_SENDTO` + `mailto:` extras) and `IosEmailLauncher` (percent-encoded `mailto:` URL + `canOpenURL`). |
| `openInAppBrowser(url)` | `@Composable expect fun` launching an in-app browser — Android Custom Tabs, iOS `SFSafariViewController`. |
| `AppConstants` | Shared URLs / addresses used by the General Menu: `INSTRUCTION_URL`, `PRIVACY_POLICY`, `SUPPORT_EMAIL`. |
| `AppNotificationManager` / `AppNotificationManagerImpl` | `SharedFlow`-backed singleton bus. ViewModels call `send(AppNotification)` to post a snackbar or dialog; `App()` observes and renders globally. Zero-replay — late subscribers do not receive past notifications. |
| `AppNotification` | Sealed class with two subtypes: `SnackBar` (message + optional action label + callback) and `Dialog` (title, message, confirm/dismiss labels + callbacks). |
| `TextProvider` | Sealed class wrapping either a raw `String` (`TextProvider.Text`) or a Compose `StringResource` (`TextProvider.Res`). Used throughout to pass localised or dynamic strings in a platform-neutral way. |

Other notable contents:
- `Platform` — `expect fun getPlatform(): Platform` returning `ANDROID` or `IOS`.
- `CoreDimens` — shared spacing/size constants.
- `CustomLoader` — shared loading indicator composable.
- `CustomDropdownMenu` / `CustomDropdownItemModel` — shared dropdown menu composable.
- `DialogAnimation` — shared enter/exit transition for dialogs.
- `MeasurementsExt` — `pxToDp()` extension.

## DI modules

| Module val | Source set | Provides |
|---|---|---|
| `coreModule` | `commonMain` | `AppNotificationManager`, `AppNavigationManager` singletons |
| `androidCoreModule` | `androidMain` | `EmailLauncher` → `AndroidEmailLauncher(androidContext())` |
| `iosCoreModule` | `iosMain` | `EmailLauncher` → `IosEmailLauncher()` |

`EmailLauncher` needs platform context (Android `Activity`/`Context`), so it is provided by a platform module that must be loaded **before** `appModule` at Koin startup — alongside the controller and bluetooth platform modules.

## Dependencies

No project module dependencies.

## External dependencies

| Library | Why |
|---|---|
| Compose Multiplatform (Material3, Foundation, Navigation) | UI primitives and the `NavGraphBuilder`/`NavController` types used in `ScreenBuilder` |
| `androidx.browser` (Custom Tabs, androidMain) | `openInAppBrowser` actual on Android |
| Koin Core + Compose | DI; `coreModule` registers `AppNotificationManager` / `AppNavigationManager`, platform modules register `EmailLauncher` |
| `navigation-compose` (Jetbrains) | `NavGraphBuilder` / `NavController` in `ScreenBuilder` |
| Kermit (`co.touchlab:kermit`) | KMP logging library used across feature modules |

## Testing

`AppNotificationManagerImplTest` in `commonTest` covers:
- Single notification delivery to one collector
- Broadcast to multiple concurrent collectors
- Zero-replay guarantee (late subscriber receives nothing)
- Emission order preservation across multiple sequential sends

Uses `kotlin.test`, `kotlinx-coroutines-test`, `runTest` + `yield()` + `async { flow.first() }` pattern.