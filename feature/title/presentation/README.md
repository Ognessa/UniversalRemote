# :feature-title-presentation

Rename-controller dialog: validates and persists a new controller title.

**Layer:** Presentation

## Key classes

| Class | What it does |
|---|---|
| `TitleEditorViewModel` | Initialised with `TitleEditorNavArgs` (containing `ControllerModel` + `TitleEditorSuccessNavigation`). Pre-fills the title field from `model.name`. On `Save`: trims whitespace, rejects blank titles with a snackbar, calls `SaveControllerUseCase`, then emits either `Close` or `OpenList` based on `args.successNavigation`. On `Cancel` emits `Close`. |
| `TitleEditorScreenState` | `data class`; single field: `title: String`. |
| `TitleEditorEvents` | Sealed interface: `TitleChanged(value)`, `Save`, `Cancel`. |
| `TitleEditorNavigation` | Sealed interface: `Close`, `OpenList`. `companion object.from(TitleEditorSuccessNavigation)` maps the enum to the sealed type. |
| `SaveControllerUseCase` | Calls `repository.insertController(model): Result<Unit>` (upsert). |
| `TitleEditorDialogBuilderImpl` | Deserialises `TitleEditorNavArgs` from the path segment via `titleEditorNavArgsFromNavArg`, passes to ViewModel via Koin `parametersOf`. On `OpenList` navigates to `ControlsListScreenBuilder.routeName` and clears the back stack to it. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | `AppNotificationManager`, `ScreenBuilder`, `TextProvider` |
| `:feature-controller` | `ControllerRepository`, `ControllerModel` |
| `:feature-title-api` | `TitleEditorDialogBuilder`, `TitleEditorNavArgs`, `TitleEditorSuccessNavigation` |

## Testing (`commonTest`)

**`TitleEditorViewModelTest`** — mocks `ControllerRepository` with Mokkery, uses real `SaveControllerUseCase` and `AppNotificationManagerImpl`:
- Initial state pre-fills title from `model.name`
- `TitleChanged` updates the title
- `Save` trims whitespace before calling the repository
- `Save` with blank title sends an error snackbar and does not call the repository
- `Save` failure sends an error snackbar
- `Save` success with `CLOSE` emits `TitleEditorNavigation.Close`
- `Save` success with `OPEN_LIST` emits `TitleEditorNavigation.OpenList`
- `Cancel` emits `TitleEditorNavigation.Close`