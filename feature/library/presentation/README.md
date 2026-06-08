# :feature-library-presentation

Element library picker — shows the available controller element types and adds a selected element to the editor canvas.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `EditorLibraryViewModel` | Receives `EditorLibraryUserEvents.ElementSelected(element)`, calls `element.createElementWithNewId()` to generate a fresh ID, emits the new element to `EditorSharedState`, then emits `EditorLibraryNavigation.CloseLibrary`. No state flow — the screen is purely reactive on user input. |
| `EditorLibraryUserEvents` | Sealed interface with one event: `ElementSelected(element: ControllerElementModel)`. |
| `EditorLibraryNavigation` | Sealed interface with one event: `CloseLibrary`. |
| `EditorLibraryScreenBuilderImpl` | Registers the route in the nav graph. On `CloseLibrary` pops the back stack. |

The screen itself renders `defaultControllerElementsList` from `:feature-controller` — currently three items: `SquareButtonModel`, `XboxButtonClusterModel`, `SliderModel`.

## Dependencies

| Module | Why |
|---|---|
| `:core` | `ScreenBuilder` |
| `:feature-controller` | `ControllerElementModel`, `defaultControllerElementsList`, `createElementWithNewId()` |
| `:feature-library-api` | `EditorLibraryScreenBuilder` contract |
| `:feature-editor-api` | `EditorSharedState` (to deliver the selected element to the editor) |

## Testing (`commonTest`)

**`EditorLibraryViewModelTest`** — mocks `EditorSharedState` with Mokkery:
- `ElementSelected` calls `editorSharedState.emitElement()` with an element that has a **different** ID from the original (verifies `createElementWithNewId()` was called) but the same `serialName`
- `EditorLibraryNavigation.CloseLibrary` is emitted after the element is sent