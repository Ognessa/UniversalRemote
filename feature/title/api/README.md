# :feature-title-api

Navigation contract and nav arg types for the rename-controller dialog.

**Layer:** Feature API

## Key classes

| Class / Interface | What it does |
|---|---|
| `TitleEditorDialogBuilder` | Extends `ScreenBuilder`; fixes `routeName = "TitleEditorDialog"`. |
| `TitleEditorNavArgs` | `@Serializable data class` bundling the `ControllerModel` to edit and a `TitleEditorSuccessNavigation` enum value. Serialised to Base64-URL-safe JSON for passing as a nav arg string. Provides `toNavArg()` (encode) and `titleEditorNavArgsFromNavArg(arg)` (decode). |
| `TitleEditorSuccessNavigation` | Enum with two values: `CLOSE` (close dialog on save) and `OPEN_LIST` (navigate to the controller list on save). Lets callers control the post-save destination. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` base interface |
| `:feature-controller` | `ControllerModel` (included in `TitleEditorNavArgs`) |

## Testing (`commonTest`)

**`TitleEditorNavArgsTest`** — serialization round-trips via `toNavArg()` → `titleEditorNavArgsFromNavArg()`:
- Round-trip with `CLOSE` navigation and default elements
- Round-trip with `OPEN_LIST` navigation
- Preserves name, `canvasRatio`, and orientation
- Preserves all element types from `defaultControllerElementsList`