# :feature-controller

Data and domain layer for controllers, plus the shared element model hierarchy and canvas rendering composable used by all other feature modules.

**Layer:** Data + Domain + shared presentation models

## Key classes

| Class / Interface | What it does |
|---|---|
| `ControllerRepository` | Domain interface. Four operations: `getAllControllers(): Flow<Result<List<ControllerModel>>>`, `getControllerById(id): Result<ControllerModel>`, `insertController(model): Result<Unit>`, `removeController(id): Result<Unit>`. |
| `ControllerRepositoryImpl` | Wraps `Database`; runs queries on `Dispatchers.IO`; maps exceptions to `Result.failure`. |
| `Database` | Internal class over the SQLDelight-generated `ControllerDatabase`. Handles the `controllers`/`elements` join and maps flat rows to `ControllerModel` via `FullControllerDao`. Elements are stored as JSON strings; deserialized lazily via `controllerJson`. |
| `DatabaseDriverFactory` | `expect class`; provides `SqlDriver`. Android actual uses `AndroidSqliteDriver`; iOS actual uses `NativeSqliteDriver`. |
| `ControllerModel` | `@Serializable data class` representing a single controller: `id`, `name`, `canvasRatio`, `orientation` (`PORTRAIT`/`LANDSCAPE`), `elements: List<ControllerElementModel>`. |
| `ControllerElementModel` | `@Serializable abstract class` — the base for all element types. Declares `id`, `displayParameters: NormalizedDisplay`, `jsonVersion`, `serialName`, and abstract methods `createElementWithNewId()`, `getDefaultSize()`, `validate()`, `changeDisplayParameters()`. |
| `SquareButtonModel` | Concrete element; `@SerialName("SquareButton")`; has `name` and `ButtonConfigModel` (hold-to-repeat config). |
| `XboxButtonClusterModel` | Concrete element; `@SerialName("XboxButtonCluster")`; four-directional button cluster. |
| `SliderModel` | Concrete element; `@SerialName("Slider")`; has `SliderConfigModel` with min/max/step validation. |
| `NormalizedDisplay` | Stores element position and scale as normalised [0,1] `Offset` + `Size` relative to the canvas. Coordinates are absolute on canvas at runtime. |
| `ControllerCanvas` | Composable that renders a `ControllerModel`. Accepts a `ControllerRenderMode` (`EDIT` or `PLAY`) which controls drag handles, selection highlight, and signal emission. |
| `controllerJson` | `Json` instance with the polymorphic `SerializersModule` for `ControllerElementModel`. `classDiscriminator = "type"`. Used for element serialization to DB and nav args. |

## Dependencies

| Module | Why |
|---|---|
| `:core` | Shared utilities |

## External dependencies

| Library | Why |
|---|---|
| SQLDelight (`android-driver`, `native-driver`, `coroutines-extensions`) | Persistence; `asFlow().mapToList()` for reactive queries |
| `kotlinx-serialization-json` | Polymorphic element serialization to/from JSON strings stored in the DB |
| Kermit (`co.touchlab:kermit`) | KMP logging inside `ControllerRepositoryImpl` |

## Database schema

Two tables in `ControllerDatabase.sq`:
- `controllers(id, name, orientation, canvas_ratio)` — one row per controller
- `elements(id, parent_id, type, jsonVersion, json)` — one row per element; `parent_id` foreign-keys to `controllers(id)` with `ON DELETE CASCADE`

There are no migration files. Schema is at version 1.

## Testing (`commonTest`)

**`ControllerRepositoryTest`** — uses a real in-memory SQLite driver (not mocked):
- Insert and retrieve a controller round-trip
- Ordering by name (ascending)
- Removal cascade
- `getControllerById` returns failure for unknown ID

**`ControllerElementSerializationTest`** — JSON and nav-arg round-trip for all three element types.

**`NormalizedDisplaySerializersTest`** — round-trip for custom `OffsetSerializer` and `SizeSerializer`.

**`SliderConfigModelTest`** — validates `SliderConfigModel.validate()` boundary conditions.

**`TextFieldExtTest`** — tests text-field utility extensions.

The in-memory driver is provided by `expect fun createTestDatabaseDriver(): SqlDriver` — actual in `androidUnitTest` uses `JdbcSqliteDriver.IN_MEMORY`; actual in `iosTest` uses `inMemoryDriver(ControllerDatabase.Schema)`.