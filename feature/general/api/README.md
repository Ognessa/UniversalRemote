# :feature-general-menu-api

Navigation contract for the General Menu, rendered inside the app's navigation drawer.

**Layer:** Feature API

Single interface: `GeneralMenuScreenBuilder`, extends `NavDrawerScreenBuilder` (not `ScreenBuilder`), initiates `routeName = "GeneralMenu"`. No implementation here.

Because the General Menu lives in the navigation drawer rather than the `NavHost`, it implements `NavDrawerScreenBuilder` (which exposes `@Composable fun Content(navController)`) instead of the route-based `ScreenBuilder`.

## Dependencies

| Module | Why |
|---|---|
| `:core` | `NavDrawerScreenBuilder` base interface |

## Testing

No tests.