# :feature-general-menu-presentation

General Menu drawer content: a list of support/info actions — "How to use", "Ask for help", and "Privacy policy" — plus the app version footer.

**Layer:** Presentation

This feature has no ViewModel or screen state. It is a stateless drawer panel: each menu item maps to a `GeneralMenuNavigation` intent that the builder forwards to the shared `AppNavigationManager`, which the app shell consumes to open the in-app browser or the device mail composer.

## Key classes

| Class | What it does |
|---|---|
| `GeneralMenuScreen` | Stateless `Column` of `MenuItem`s. Receives a `navigate: (CoroutineScope, GeneralMenuNavigation) -> Unit` lambda and passes the current `lifecycleScope` so the builder can emit suspending events. Renders the app version from `BuildKonfig` in the footer. |
| `MenuItem` | Internal row composable: leading icon + label, full-width clickable. |
| `GeneralMenuNavigation` | Sealed class of menu intents: `OpenInstruction`, `Support`, `OpenPrivacyPolicy`. |
| `GeneralMenuScreenBuilderImpl` | Implements `GeneralMenuScreenBuilder`. Maps each `GeneralMenuNavigation` to an `AppNavigationManager` event — `OpenBrowser(INSTRUCTION_URL / PRIVACY_POLICY)` for the links, `OpenEmail(SUPPORT_EMAIL)` for support — and emits it on the given scope. |

The three constants (`INSTRUCTION_URL`, `PRIVACY_POLICY`, `SUPPORT_EMAIL`) live in `AppConstants` in `:core`, so the launcher and recipient stay parameterized rather than hardcoded in this module.

## Dependencies

| Module | Why |
|---|---|
| `:core` | `NavDrawerScreenBuilder`, `AppNavigationManager`, `AppNavigationEvent`, `AppConstants` |
| `:feature-general-menu-api` | `GeneralMenuScreenBuilder` contract |

## Testing

No tests in this module.