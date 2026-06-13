# ADR 001 — SharedFlow for navigation events, StateFlow for UI state

**Status:** Accepted

## Context

ViewModels need to expose two kinds of data to the UI:

1. **Persistent UI state** — the current screen contents (list of controllers, loading flag, selected element, etc.). The UI must always have a value to render, including when it re-subscribes after a configuration change or lifecycle transition.

2. **One-shot navigation events** — commands to navigate to another screen (open editor, open device picker, close). These must fire exactly once. If the UI re-subscribes after a lifecycle drop, it must not re-receive a navigation event that was already handled — doing so would push a duplicate backstack entry.

A single `StateFlow` cannot serve both needs: it replays its last value to new collectors, so a navigation event stored in state would re-trigger navigation every time the screen recomposes or the lifecycle restarts.

## Decision

UI state is exposed as `StateFlow<XyzScreenState>` (data class with defaults). Navigation events are exposed as `MutableSharedFlow<XyzNavigation>` with default `replay = 0`, wrapped as `asSharedFlow()`.

Navigation events are collected in the Screen composable using `repeatOnLifecycle(Lifecycle.State.STARTED)`. This means:

- While the screen is in the foreground (STARTED), events are consumed immediately.
- When the lifecycle drops below STARTED (e.g. another screen is pushed), collection is suspended and resumes when STARTED is reached again.
- Because `replay = 0`, any event emitted while the lifecycle was below STARTED is not buffered and not re-delivered — this is intentional for navigation commands.

## Consequences

**Enables:**
- Configuration-change safety: UI state survives because `StateFlow` always holds the current value.
- Exactly-once navigation: `SharedFlow` with `replay = 0` ensures navigation events are not redelivered to re-subscribing collectors.
- Clear separation of concerns in the ViewModel: state mutations go through `_state.update {}`, navigation goes through `_events.emit()`.

**Constrains:**
- Navigation events emitted while the lifecycle is below STARTED are silently dropped. This is acceptable for navigation (the user is not on that screen) but means ViewModels must not emit events that need guaranteed delivery during background states.
- Tests must use `async { viewModel.events.collect { … } }` + `yield()` to set up the collector before triggering the event, otherwise the zero-replay flow delivers nothing.
