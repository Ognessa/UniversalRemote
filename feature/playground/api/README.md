# :feature-playground-api

Navigation contract for the playground screen.

**Layer:** Feature API

Single interface: `PlaygroundScreenBuilder`, extends `ScreenBuilder`, fixes `routeName = "Playground"`. The route accepts a required `id` path segment (`"Playground/{id}"`) identifying the controller to run. No implementation here.

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` base interface |

## Testing

No tests.