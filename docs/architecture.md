# Color Link Architecture

## Architecture style

Color Link uses a **ViewModel-based MVVM shell with MVI contracts and Clean Architecture boundaries**.

This means:

- Compose renders immutable state.
- User actions are sent as intents.
- ViewModels reduce state and emit one-time events.
- UseCases hold business operations.
- Repositories abstract persistence and external data.
- Data layer implementations map DTO/entity models into domain models before reaching ViewModels.

## Dependency direction

Dependencies must point inward:

```text
app
 └─ feature-impl
     ├─ feature-api
     ├─ core-ui
     ├─ core-designsystem
     ├─ core-navigation
     └─ domain
          └─ repository interfaces

data
 ├─ domain repository interfaces
 ├─ core-database
 ├─ core-network
 └─ core-common
```

The domain layer must not depend on Android framework, Compose, Ktor, Room, DataStore, or feature modules.

## Recommended modules

```text
:app
:core:common
:core:designsystem
:core:ui
:core:navigation
:core:model
:core:database
:core:network
:core:testing

:domain:game
:domain:level
:domain:settings

:data:game
:data:level
:data:settings

:feature:onboarding-api
:feature:onboarding-impl
:feature:home-api
:feature:home-impl
:feature:level-selection-api
:feature:level-selection-impl
:feature:gameplay-api
:feature:gameplay-impl
:feature:settings-api
:feature:settings-impl
```

For an MVP, modules may be collapsed, but package boundaries must remain clean.

## Package structure

### Feature implementation module

```text
feature/gameplay-impl/src/main/kotlin/<package>/feature/gameplay/
  di/
    GameplayFeatureDIModule.kt
  presentation/
    GameplayContract.kt
    GameplayViewModel.kt
    GameplayScreen.kt
  components/
    GameBoard.kt
    DotNode.kt
    GameControls.kt
    LevelCompleteDialog.kt
  mapper/
    GameplayUiMapper.kt
```

### Feature API module

```text
feature/gameplay-api/src/main/kotlin/<package>/feature/gameplay/api/
  GameplayRoute.kt
  GameplayNavigationApi.kt
```

### Domain module

```text
domain/game/src/main/kotlin/<package>/domain/game/
  model/
    BoardPosition.kt
    Dot.kt
    DotColor.kt
    ColorPath.kt
    Level.kt
    MoveResult.kt
  repository/
    GameRepository.kt
    LevelRepository.kt
  usecase/
    StartPathUseCase.kt
    ExtendPathUseCase.kt
    ValidateMoveUseCase.kt
    CheckWinConditionUseCase.kt
    RestartLevelUseCase.kt
    UseHintUseCase.kt
```

### Data module

```text
data/game/src/main/kotlin/<package>/data/game/
  di/
    GameDataDIModule.kt
  repository/
    GameRepositoryImpl.kt
  datasource/
    LocalLevelDataSource.kt
    GameProgressDataSource.kt
  local/
    entity/
      LevelProgressEntity.kt
    dao/
      LevelProgressDao.kt
  mapper/
    LevelEntityMapper.kt
    LevelAssetMapper.kt
```

## State management contract

Each feature must expose three types:

```kotlin
data class FeatureState(...)

sealed interface FeatureIntent {
    data object ScreenOpened : FeatureIntent
}

sealed interface FeatureEvent {
    data object NavigateBack : FeatureEvent
}
```

For gameplay:

```kotlin
data class GameplayState(
    val isLoading: Boolean = false,
    val level: Level? = null,
    val paths: List<ColorPath> = emptyList(),
    val activePath: ColorPath? = null,
    val selectedColor: DotColor? = null,
    val moveCount: Int = 0,
    val hintCount: Int = 3,
    val isLevelComplete: Boolean = false,
    val errorMessage: String? = null
)

sealed interface GameplayIntent {
    data class LoadLevel(val levelId: String) : GameplayIntent
    data class StartDrag(val position: BoardPosition) : GameplayIntent
    data class DragTo(val position: BoardPosition) : GameplayIntent
    data object EndDrag : GameplayIntent
    data object Undo : GameplayIntent
    data object Restart : GameplayIntent
    data object UseHint : GameplayIntent
    data object BackClicked : GameplayIntent
    data object SettingsClicked : GameplayIntent
}

sealed interface GameplayEvent {
    data object NavigateBack : GameplayEvent
    data object OpenSettings : GameplayEvent
    data object PlayWinAnimation : GameplayEvent
    data object PlayInvalidMoveFeedback : GameplayEvent
    data class ShowError(val message: String) : GameplayEvent
}
```

## ViewModel rules

All ViewModels must:

- Extend `StateViewModel` if available in the reference architecture.
- Expose a single immutable state stream.
- Expose a single one-time event stream.
- Handle actions through `handleIntent(intent)`.
- Delegate gameplay validation to UseCases.
- Use repositories/use cases instead of direct persistence or network calls.

Example shape:

```kotlin
class GameplayViewModel(
    private val loadLevelUseCase: LoadLevelUseCase,
    private val startPathUseCase: StartPathUseCase,
    private val extendPathUseCase: ExtendPathUseCase,
    private val checkWinConditionUseCase: CheckWinConditionUseCase,
    private val saveProgressUseCase: SaveProgressUseCase
) : StateViewModel<GameplayState, GameplayIntent, GameplayEvent>(GameplayState()) {

    override fun handleIntent(intent: GameplayIntent) {
        when (intent) {
            is GameplayIntent.LoadLevel -> loadLevel(intent.levelId)
            is GameplayIntent.StartDrag -> startDrag(intent.position)
            is GameplayIntent.DragTo -> dragTo(intent.position)
            GameplayIntent.EndDrag -> endDrag()
            GameplayIntent.Undo -> undo()
            GameplayIntent.Restart -> restart()
            GameplayIntent.UseHint -> useHint()
            GameplayIntent.BackClicked -> sendEvent(GameplayEvent.NavigateBack)
            GameplayIntent.SettingsClicked -> sendEvent(GameplayEvent.OpenSettings)
        }
    }
}
```

If the exact `StateViewModel` signature in the reference project differs, follow the existing base class API while preserving this contract style.

## Domain layer rules

Domain contains:

- Business models
- Repository interfaces
- UseCases
- Pure game validation rules

Domain must not contain:

- Compose classes
- Android `Context`
- Resource IDs
- Room entities
- Ktor response models
- DataStore preferences keys
- UI strings

## Data layer rules

Data contains:

- Repository implementations
- Local data sources
- Remote data sources if needed
- DTOs
- Entities
- Mappers

Data must map everything into domain models before returning values.

Example:

```kotlin
class LevelRepositoryImpl(
    private val localLevelDataSource: LocalLevelDataSource,
    private val progressDataSource: GameProgressDataSource
) : LevelRepository {

    override suspend fun getLevel(levelId: String): Level {
        return localLevelDataSource.getLevel(levelId).toDomain()
    }
}
```

## Ktor networking rules

Use Ktor only where external data is needed, for example:

- cloud level packs
- remote config
- leaderboard
- account sync
- ads/reward validation if implemented later

All network calls should use an `ApiResponse<T>` wrapper.

```kotlin
sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Error(val message: String, val throwable: Throwable? = null) : ApiResponse<Nothing>
}
```

Use `safeApiCall` in repositories or shared network helpers, not inside composables.

## Local persistence

Use local persistence for:

- Completed levels
- Best move count
- Current level
- Hint count
- Sound enabled
- Haptics enabled
- Theme mode
- Onboarding completion

Recommended options:

- Room for structured progress/history
- DataStore for settings and simple preferences
- JSON assets for bundled level definitions

## Navigation architecture

Feature API modules expose route contracts. Feature implementation modules register composable destinations.

Example:

```kotlin
object GameplayRoute {
    const val route = "gameplay/{levelId}"
    fun create(levelId: String): String = "gameplay/$levelId"
}
```

Navigation events must be emitted from ViewModels and handled by the screen/nav host.

Do not pass NavController deep into reusable components.

## UI model strategy

Domain models can be used directly in gameplay rendering if they are already UI-safe and stable. For complex screens, map domain models to UI models.

Recommended:

```kotlin
data class LevelCardUiModel(
    val levelId: String,
    val number: Int,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val stars: Int
)
```

Do not store Android string resources in domain models.

## Error handling

- Use domain-specific result types for expected gameplay outcomes.
- Use `ApiResponse<T>` for network results.
- Use typed failures where possible.
- Use `showError(message)` or an equivalent event helper for UI error messages.

Gameplay invalid moves should usually produce local feedback, not blocking error dialogs.

## Testing rules

Prioritize unit tests for:

- valid path extension
- invalid diagonal move
- blocked occupied cell
- trying to pass through another color dot
- completing a path
- clearing/replacing an existing path of same color
- win condition when all cells are filled
- no win when pairs are connected but empty cells remain

Use Compose tests for:

- screen loading state
- level cards locked/unlocked state
- gameplay controls visible
- level complete dialog appears

## Agent checklist before code changes

Before editing code, identify:

- affected module
- affected package
- state changes
- new intents/events
- domain rules affected
- persistence impact
- navigation impact
- tests needed

After editing code, verify:

- no data model leaked into UI
- no business logic added to composables
- no hard-coded design tokens
- no duplicate DI binding
- no broken module dependency direction
