AGENTS.md — Color Link Android Project Instructions
Project role
You are an Android Studio coding agent building Color Link, a premium offline-first Android puzzle game where users connect matching colored dots with continuous paths until the board is fully filled.
The project must be implemented as a production-ready Android app using:
Kotlin
Jetpack Compose
MVI-style state management
Clean Architecture principles
Koin for dependency injection
Kotlin Coroutines and Flow
Local-first/offline-first data storage
The app must work fully without internet. Do not add networking unless explicitly requested later.
Before changing code, read and follow:
```text
AGENTS.md
docs/design-system.md
docs/architecture.md
docs/gameplay-rules.md
docs/compose-ui.md
```
If any instruction conflicts, follow this priority:
```text
Current user request
→ AGENTS.md
→ docs/architecture.md
→ docs/gameplay-rules.md
→ docs/design-system.md
→ docs/compose-ui.md
```
---
Current architecture decision
This project uses a simplified 4-module architecture for MVP.
Use these modules only:
```text
:app
:core
:domain
:data
```
The goal is to keep the app clean and scalable without over-engineering the first version.
---
Module responsibilities
`:app`
Contains app-level Android code and feature UI packages.
Responsibilities:
`MainActivity`
Application class
Compose navigation graph
Koin startup and DI aggregation
Feature screens
Feature ViewModels
Feature MVI contracts
App-level routing
Screen-level state collection
Recommended package structure:
```text
app/src/main/java/com/example/colorlink/
  app/
  di/
  navigation/
  feature/
    onboarding/
    home/
    levels/
    gameplay/
    settings/
```
Feature package structure:
```text
feature/<feature-name>/
  <Feature>Screen.kt
  <Feature>ViewModel.kt
  <Feature>Contract.kt
  components/
  di/
```
Example:
```text
feature/gameplay/
  GameplayScreen.kt
  GameplayViewModel.kt
  GameplayContract.kt
  components/
    GameBoard.kt
    GameplayTopBar.kt
    GameplayControls.kt
  di/
    GameplayModule.kt
```
---
`:core`
Contains shared infrastructure and reusable UI/design-system code.
Responsibilities:
`StateViewModel`
MVI base helpers
Result/error helpers
Common utilities
Theme
Colors
Typography
Spacing
Shapes
Reusable Compose components
Design-system components
Shared animation helpers
Recommended package structure:
```text
core/src/main/java/com/example/colorlink/core/
  mvi/
  common/
  ui/
    theme/
    components/
    animation/
    modifier/
```
`StateViewModel` belongs here.
---
`:domain`
Contains pure Kotlin business logic.
Responsibilities:
Domain models
Repository interfaces
Use cases
Gameplay validation rules
Win condition logic
Move validation
Board/path rules
Recommended package structure:
```text
domain/src/main/java/com/example/colorlink/domain/
  model/
  repository/
  usecase/
  logic/
```
Domain must not depend on:
Android framework
Compose
Ktor
Room
DataStore
JSON/serialization annotations
DTOs
UI state classes
---
`:data`
Contains offline-first local data implementations.
Responsibilities:
Local JSON level loading
Assets reader
DTOs
Mappers
DataStore progress storage
DataStore settings storage
Repository implementations
Local data sources
Recommended package structure:
```text
data/src/main/java/com/example/colorlink/data/
  local/
  assets/
  datastore/
  dto/
  mapper/
  repository/
  di/
```
Data layer must map all data models to domain models before returning anything to `:app`.
---
Architecture summary
The project uses a ViewModel-based MVVM shell with MVI state handling and Clean Architecture boundaries.
Required flow:
```text
Composable Screen
    ↓ sends Intent
ViewModel / StateViewModel
    ↓ calls UseCase or Repository interface
Domain UseCase
    ↓ calls Repository interface
Data Repository implementation
    ↓ uses local assets / DataStore
Local Data Source
    ↓ maps DTO/local model to Domain model
Domain Model
    ↓ returns to ViewModel
UI State
```
State flows downward. Intents and callbacks flow upward.
Do not bypass layers.
---
Offline-first rules
Color Link is offline-first.
Core gameplay must never depend on internet availability.
Required rules:
Levels are bundled locally as JSON assets.
User progress is stored locally.
Settings are stored locally.
Hints, completed levels, stars, moves, streaks, and settings must work offline.
Local data is the source of truth.
Do not add Ktor/networking in MVP.
Do not block any screen behind internet checks.
Do not create API modules unless explicitly requested later.
If networking is added later, it must be optional sync only. Remote data should sync into local storage first, and UI should continue reading from local/domain repositories.
---
Non-negotiable rules
Use Jetpack Compose only for UI.
Do not create XML layouts.
Follow Unidirectional Data Flow.
Use MVI-style contracts: `State`, `Intent`, and `Event`.
Keep UI, domain, data, and core concerns separate.
Do not place gameplay rules directly inside composables.
Do not place repository implementation logic directly inside ViewModels.
Do not expose DTOs, DataStore models, persistence models, or JSON models to UI.
Do not use `LiveData`; use Compose state, `StateFlow`, `SharedFlow`, and Kotlin `Flow`.
Feature ViewModels must extend `StateViewModel`.
Every user action must be represented as an `Intent` and routed through `handleIntent(intent)`.
One-time actions such as navigation, snackbars, dialogs, haptics, and sound triggers must be emitted as `Event`s.
Preserve the premium dark, glass, neon-glow visual language defined in `docs/design-system.md`.
---
Feature contract style
Each feature in `:app` should define a contract file.
Example:
```kotlin
data class GameplayState(
    val isLoading: Boolean = false,
    val level: Level? = null,
    val paths: List<ColorPath> = emptyList(),
    val selectedColor: DotColor? = null,
    val activePath: ColorPath? = null,
    val moveCount: Int = 0,
    val hintCount: Int = 3,
    val errorMessage: String? = null,
    val isPaused: Boolean = false,
    val isLevelComplete: Boolean = false
)

sealed interface GameplayIntent {
    data class LoadLevel(val levelId: String) : GameplayIntent
    data class StartDrag(val position: BoardPosition) : GameplayIntent
    data class DragTo(val position: BoardPosition) : GameplayIntent
    data object EndDrag : GameplayIntent
    data object Undo : GameplayIntent
    data object Restart : GameplayIntent
    data object UseHint : GameplayIntent
    data object PauseClicked : GameplayIntent
    data object ResumeClicked : GameplayIntent
    data object BackClicked : GameplayIntent
    data object SettingsClicked : GameplayIntent
    data object ContinueClicked : GameplayIntent
}

sealed interface GameplayEvent {
    data object NavigateBack : GameplayEvent
    data object OpenSettings : GameplayEvent
    data object NavigateToNextLevel : GameplayEvent
    data object PlayWinAnimation : GameplayEvent
    data class ShowError(val message: String) : GameplayEvent
}
```
Rules:
`State` represents persistent UI state.
`Intent` represents user actions or lifecycle requests.
`Event` represents one-time effects.
Do not store one-time events permanently inside state.
Do not trigger navigation directly from composables without passing through the ViewModel/event flow unless it is a pure app-level route wrapper.
---
ViewModel rules
All feature ViewModels must extend `StateViewModel`.
Allowed ViewModel responsibilities:
Receive intents
Call use cases
Call repository interfaces when use case would be unnecessary
Update immutable UI state
Emit one-time events
Handle loading/error/completion state
Not allowed in ViewModels:
Gameplay validation logic
JSON parsing
DataStore implementation details
DTO usage
Direct Android `Context` usage
Direct persistence logic
Network calls
Use immutable state updates.
All user interactions must go through:
```kotlin
handleIntent(intent)
```
---
UI rules
Use Jetpack Compose only.
Screens should be mostly stateless:
```text
Route composable
  - obtains ViewModel
  - collects state
  - collects events
  - passes state and onIntent to Screen

Screen composable
  - renders UI
  - sends intents through callbacks
  - contains no business logic
```
Example structure:
```kotlin
@Composable
fun GameplayRoute(
    levelId: String,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: GameplayViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameplayScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
```
Rules:
Use design tokens instead of hard-coded colors and dimensions.
Use reusable components from `:core`.
Keep composables previewable where practical.
Do not add fragments or XML.
Do not perform repository calls from composables.
Do not perform gameplay validation inside composables.
---
Compose Canvas gameplay rules
The gameplay board should use Compose Canvas or a dedicated custom Compose component.
Canvas responsibilities:
Render grid
Render dots
Render completed paths
Render active dragging path
Convert pointer coordinates to `BoardPosition`
Send `StartDrag`, `DragTo`, and `EndDrag` intents
Canvas must not:
Validate moves
Decide win condition
Save progress
Access repositories
Mutate domain state directly
Gameplay logic belongs in `:domain`.
---
Required game domain concepts
Use pure Kotlin domain models similar to the existing implementation.
Expected concepts:
```text
BoardPosition
DotColor
Dot
ColorPath
Level
LevelProgress
CellOccupancy
MoveResult
```
Domain models should stay independent of Compose, Android framework, Ktor, Room, DataStore, and JSON serialization.
Gameplay logic should support:
Valid board position checks
Orthogonal movement only
No diagonal movement
Path validation
Backtracking
Path trimming
Path replacement for same color
Preventing crossings
Preventing entering different colored dots
Completed pair detection
Full-board win condition
---
Data layer rules
The app is local-first.
Use local JSON assets for bundled levels.
Example:
```text
data/src/main/assets/levels/
```
Use DataStore for simple local state:
Completed levels
Stars
Best moves
Best time if supported
Hint count if supported
Sound/music/haptics settings
Use Room only if progress/history becomes relational or complex. Do not add Room unless needed.
Data layer rules:
DTOs stay in `:data`.
DataStore models stay in `:data`.
Mappers convert data models to domain models.
Repositories implement interfaces from `:domain`.
UI and ViewModels must never consume DTOs directly.
Recommended repositories:
```text
LevelRepository
ProgressRepository
SettingsRepository
```
If the current code already has a `GameRepository`, keep it only if it has a clear responsibility. Prefer separating level loading, progress, and settings if the repository becomes too large.
---
Dependency injection
Use Koin.
Use separate modules by responsibility.
Recommended structure:
```text
app/di/AppModule.kt
core/di/CoreModule.kt
domain/di/DomainModule.kt
data/di/DataModule.kt
app/feature/gameplay/di/GameplayModule.kt
app/feature/home/di/HomeModule.kt
app/feature/levels/di/LevelsModule.kt
app/feature/settings/di/SettingsModule.kt
```
Rules:
`:app` starts Koin and aggregates modules.
`:core` provides reusable shared dependencies only if needed.
`:domain` provides use cases.
`:data` binds repository implementations to domain repository interfaces.
Feature packages register ViewModels.
Do not create one giant DI module if avoidable.
Do not resolve dependencies manually inside composables.
---
Navigation rules
Use Compose Navigation.
Minimum app flow:
```text
Splash / Onboarding
→ Home
→ Levels
→ Gameplay
→ Level Complete
→ Settings
```
Gameplay is a focused screen. Hide global bottom navigation on gameplay unless explicitly requested.
Main screens may use bottom navigation:
Home
Levels
Settings
Do not show bottom navigation on:
Gameplay
Level complete overlay/screen
Pause dialog
Onboarding
Splash
---
Design system rules
Follow the premium dark/neon/glass visual language.
Use:
Dark navy background
Glass panels for major surfaces
Bright distinguishable dot colors
Rounded corners
Subtle glow
Smooth but minimal animations
Consistent spacing and typography
Do not invent a new visual style.
Do not hard-code random colors in feature screens.
Use `docs/design-system.md` and `:core` UI tokens/components.
---
Naming conventions
ViewModels
```text
[Feature]ViewModel
```
Examples:
```text
HomeViewModel
LevelsViewModel
GameplayViewModel
SettingsViewModel
```
Contracts
```text
[Feature]State
[Feature]Intent
[Feature]Event
```
Use cases
```text
[Action][Entity]UseCase
```
Examples:
```text
ValidateMoveUseCase
CheckWinConditionUseCase
StartPathUseCase
LoadLevelsUseCase
SaveLevelProgressUseCase
```
Repositories
```text
[Entity]Repository
[Entity]RepositoryImpl
```
Examples:
```text
LevelRepository
LevelRepositoryImpl
ProgressRepository
ProgressRepositoryImpl
SettingsRepository
SettingsRepositoryImpl
```
Mappers
Use either clear mapper classes or extension functions:
```text
LevelDto.toDomain()
ProgressData.toDomain()
LevelProgress.toData()
```
Composables
Use PascalCase:
```text
HomeScreen
LevelsScreen
GameplayScreen
GameBoard
PrimaryGameButton
GlassPanel
```
---
Implementation quality bar
Before finalizing code, ensure:
The project builds.
Relevant tests pass.
Composables are previewable where practical.
UI uses design tokens instead of hard-coded colors and dimensions.
State is immutable.
Events are not stored as permanent state.
Domain logic is unit-testable without Android framework classes.
Invalid gameplay moves are handled gracefully.
The gameplay board supports different grid sizes.
Touch handling works for fast drag gestures, not only slow cell-by-cell taps.
Local JSON and DataStore logic remain inside `:data`.
DTOs do not leak into `:app` or `:domain`.
---
Testing expectations
Prioritize tests for domain gameplay logic.
Required test areas:
Valid adjacent movement
Diagonal movement invalid
Out-of-board movement invalid
Crossing another path invalid
Entering a different colored dot invalid
Backtracking
Path trimming
Path replacement
Completed pair detection
Win condition requires all pairs connected
Win condition requires all playable cells filled
Undo behavior
Restart behavior
JSON parsing/mapping
Progress save/load
Domain tests must not require Android framework classes.
---
What to do when asked to add a feature
Confirm the task scope.
Check `AGENTS.md` and relevant docs.
Identify affected packages/modules.
Update or create the feature contract: `State`, `Intent`, `Event`.
Add or update domain models/use cases if business logic is involved.
Add repository methods only if persistence or external data is required.
Implement ViewModel intent handling.
Implement stateless composables.
Wire navigation and DI.
Add or update tests.
Run build/tests.
Summarize files changed and remaining TODOs.
---
What not to do
Do not rewrite the entire app unless explicitly asked.
Do not invent a new visual style.
Do not replace MVI contracts with callback-heavy UI logic.
Do not use XML layouts.
Do not use fragments for new UI.
Do not make network calls directly from ViewModels.
Do not add Ktor/networking unless explicitly requested.
Do not use DTOs as UI models.
Do not use `Context` inside domain logic.
Do not add unnecessary libraries for simple game logic.
Do not create unnecessary modules.
Do not move gameplay rules into UI or ViewModel.