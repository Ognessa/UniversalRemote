# :feature-list-presentation

Presents the list of saved controllers with create, duplicate, rename, edit, and delete actions.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `ControlsListViewModel` | Loads all controllers via `GetAllControllersUseCase`, handles CRUD actions, emits `ControlsListNavigation` events (open editor, open playground, open title dialog). |
| `ControlsListScreenState` | `data class`; fields: `isLoading: Boolean`, `controllersList: List<ControllerModel>`. |
| `ControlsListEvents` | Sealed interface for user actions: `Refresh`, `CreateNew`, and nested `Item` (Clicked, Rename, Edit, Duplicate, Delete). |
| `ControlsListNavigation` | Sealed interface for navigation: `OpenEditor(id?)`, `OpenTitleEditor(model)`, `OpenPlayground(id)`. |
| `ControlsListScreenBuilderImpl` | Implements `ControlsListScreenBuilder`; wires the composable into the nav graph; handles `ControlsListNavigation` by calling `navController.navigate(...)` with appropriate routes. |
| `GetAllControllersUseCase` | Returns `Flow<Result<List<ControllerModel>>>` from `ControllerRepository`. |
| `DuplicateControllerUseCase` | Deep-copies a controller: new `id` for the controller and `createElementWithNewId()` for every element, then inserts. |
| `DeleteControllerUseCase` | Calls `repository.removeController(id)`. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `AppNotificationManager`, `ScreenBuilder`, `TextProvider` |
| `:feature-controller` | `ControllerRepository`, `ControllerModel` |
| `:feature-list-api` | `ControlsListScreenBuilder` contract |
| `:feature-editor-api` | `ControllerEditorScreenBuilder` (needed by `ControlsListScreenBuilderImpl` to compose the edit route) |
| `:feature-title-api` | `TitleEditorDialogBuilder`, `TitleEditorNavArgs` (for rename flow) |
| `:feature-playground-api` | `PlaygroundScreenBuilder` (for open-playground navigation) |

## Testing (`commonTest`)

**`ControlsListViewModelTest`** — mocks `ControllerRepository` with Mokkery, uses real use cases:
- Initial load success populates state and clears loading
- Initial load failure sends a snackbar notification
- `CreateNew` emits `OpenEditor(id = null)`
- `Item.Edit` emits `OpenEditor(id)`
- `Item.Rename` emits `OpenTitleEditor` when the controller exists; sends notification when not found
- `Item.Duplicate` success/failure/unknown-id paths all emit appropriate notifications
- `Item.Delete` success and failure paths

**`DuplicateControllerUseCaseTest`** — mocks `ControllerRepository`:
- Repository success/failure propagated correctly
- New IDs assigned to controller and all elements
- Name, canvasRatio, and orientation preserved

All tests use `Dispatchers.setMain(UnconfinedTestDispatcher())` + `runTest` + `yield()`.