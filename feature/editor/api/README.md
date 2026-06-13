# :feature-editor-api

Navigation contract and cross-screen communication interface for the controller editor.

**Layer:** Feature API

## Key classes

| Class / Interface | What it does |
|---|---|
| `ControllerEditorScreenBuilder` | Extends `ScreenBuilder`; fixes `routeName = "ControllerEditor"`. The route accepts an optional `id` path segment (`"ControllerEditor/{id}"`) — `null` id means create-new. |
| `EditorSharedState` | Communication bus between the editor screen and its child screens (library, signal editor). Exposes `elementsFlow: SharedFlow<List<ControllerElementModel>>` and `suspend fun emitElement(element)` / `emitElements(list)`. The editor observes this flow to merge newly added or modified elements into its canvas state. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` base interface |
| `:feature-controller` | `ControllerElementModel` used in `EditorSharedState` |

## Testing

No tests in this module. `EditorSharedStateImpl` and its tests live in `:feature-editor-presentation`.