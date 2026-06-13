# :feature-editor-presentation

Drag-and-drop canvas editor for building and modifying a controller layout.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `ControllerEditorViewModel` | Loads an existing controller by ID (or starts blank), observes `EditorSharedState.elementsFlow` to merge elements coming from the library and signal-editor screens, handles all canvas interactions (select, move, duplicate, delete, orientation toggle), and persists the controller via `SaveControllerUseCase` before navigating away. |
| `ControllerEditorScreenState` | `data class`; fields: `isLoading`, `title`, `canvasSizeDp: Size`, `orientation: ControllerOrientation`, `elements: List<ControllerElementModel>`, `selectedElementId: String?`. |
| `ControllerEditorUserEvent` | Sealed interface for canvas actions: `ClearSelection`, `OrientationChanged`, `OpenLibrary`, `Save`, `CanvasSizeChanged`, and nested `ElementAction` (Clicked, Modified, OpenSignalEditor, Duplicate, Delete). |
| `ControllerEditorNavigation` | Sealed interface: `Close`, `OpenLibrary`, `OpenSignalEditor(element)`, `OpenTitleEditor(model)`. |
| `EditorSharedStateImpl` | Implements `EditorSharedState` (from `:feature-editor-api`) using a zero-replay `MutableSharedFlow`. Lives here rather than in the api module because it is an implementation detail. |
| `ControllerEditorScreenBuilderImpl` | Wires the screen into the nav graph; extracts the optional `id` path segment; resolves navigation events (library, signal editor, title dialog) to `navController.navigate(...)` calls. The signal editor nav arg is Base64-encoded JSON of the selected `ControllerElementModel`. |
| `GetControllerByIdUseCase` | Calls `repository.getControllerById(id): Result<ControllerModel>`. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `AppNotificationManager`, `ScreenBuilder`, `TextProvider` |
| `:feature-controller` | `ControllerRepository`, `ControllerModel`, `ControllerElementModel`, `ControllerCanvas` |
| `:feature-editor-api` | `ControllerEditorScreenBuilder`, `EditorSharedState` |
| `:feature-library-api` | `EditorLibraryScreenBuilder` (to compose library route) |
| `:feature-signal-api` | `SignalEditorScreenBuilder` (to compose signal-editor route) |
| `:feature-title-api` | `TitleEditorDialogBuilder`, `TitleEditorNavArgs` (for save → rename flow) |

## Testing (`commonTest`)

**`ControllerEditorViewModelTests`** — mocks `ControllerRepository` (Mokkery) + `EditorSharedState` (Mokkery); uses real `EditorSharedStateImpl` and `GetControllerByIdUseCase`:
- Load existing controller by ID populates state
- Create-new flow (no ID) starts with empty canvas
- `ElementAction.Clicked` sets `selectedElementId`
- `ElementAction.Duplicate` creates a new element with a different ID
- `ElementAction.Delete` removes the element
- `OrientationChanged` toggles between PORTRAIT and LANDSCAPE
- New elements arriving via `EditorSharedState` are merged into the canvas

**`EditorSharedStateTest`** — no mocks; tests `EditorSharedStateImpl` directly:
- Single element delivered to one collector
- List of elements delivered to multiple collectors
- Empty list emission does not trigger the flow
- Zero-replay guarantee (late subscriber receives nothing)
- Emission order preserved across `emitElement` + `emitElements` interleaved calls