# :feature-signal-presentation

Configures the signal string emitted by a single controller element when it is activated during playback.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `SignalEditorViewModel` | Initialised with a `ControllerElementModel` passed as a nav arg. Holds the element in `SignalEditorScreenState`. On `SaveChanges` emits the modified element to `EditorSharedState` and then emits `SignalEditorScreenNavigation.Close`. |
| `SignalEditorScreenState` | `data class`; single field: `element: ControllerElementModel`. |
| `SignalEditorScreenEvent` | Sealed interface: `OnElementModified(element)` (user edits a signal field) and `SaveChanges`. |
| `SignalEditorScreenNavigation` | Sealed interface with one event: `Close`. |
| `SignalEditorScreenBuilderImpl` | Deserialises the `ControllerElementModel` from the `EDITED_ELEMENT_ARG` path segment (Base64-URL-decoded JSON via `controllerModelFromNavArg`), injects it into the ViewModel via Koin `parametersOf`, and pops the back stack on `Close`. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` |
| `:feature-controller` | `ControllerElementModel`, `controllerModelFromNavArg` (nav arg deserialization) |
| `:feature-signal-api` | `SignalEditorScreenBuilder` contract |
| `:feature-editor-api` | `EditorSharedState` (to return the modified element to the editor) |

## Testing (`commonTest`)

**`SignalEditorViewModelTest`** — mocks `EditorSharedState` with Mokkery:
- Initial state holds the provided element
- `OnElementModified` updates the element in state
- `SaveChanges` calls `editorSharedState.emitElement()` with an element whose `id` matches the original
- `SaveChanges` emits `SignalEditorScreenNavigation.Close`