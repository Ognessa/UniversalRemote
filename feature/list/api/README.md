# :feature-list-api

Navigation contract for the controller list screen.

**Layer:** Feature API

This module contains one interface: `ControlsListScreenBuilder`, which extends `ScreenBuilder` from `:core` and fixes `routeName = "ControlsList"`. It has no implementation — the implementation lives in `:feature-list-presentation`.

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` base interface |

## Testing

No tests.