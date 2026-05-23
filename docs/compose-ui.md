# Compose UI Implementation Rules

## Compose-only rule

All new UI must be built with Jetpack Compose.

Do not use:

- XML layouts
- Fragments for new screens
- `findViewById`
- `LiveData` for UI state

## Screen structure

Each screen should have:

```text
<Feature>Route.kt       // collects state, handles navigation/events
<Feature>Screen.kt      // stateless UI
<Feature>Contract.kt    // State, Intent, Event
<Feature>ViewModel.kt   // intent handling and state reduction
components/*            // reusable screen components
```

Recommended pattern:

```kotlin
@Composable
fun GameplayRoute(
    viewModel: GameplayViewModel = koinViewModel(),
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                GameplayEvent.NavigateBack -> onBack()
                GameplayEvent.OpenSettings -> onOpenSettings()
                GameplayEvent.PlayWinAnimation -> Unit
                GameplayEvent.PlayInvalidMoveFeedback -> Unit
                is GameplayEvent.ShowError -> Unit
            }
        }
    }

    GameplayScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}
```

`GameplayScreen` must not directly know about repositories, use cases, or navigation controllers.

## Stateless composables

Prefer stateless composables:

```kotlin
@Composable
fun GameplayScreen(
    state: GameplayState,
    onIntent: (GameplayIntent) -> Unit,
    modifier: Modifier = Modifier
)
```

Composable callbacks should immediately send intents, for example:

```kotlin
onUndoClick = { onIntent(GameplayIntent.Undo) }
```

Do not implement game rules inside callbacks.

## Theme usage

All UI must use tokens from `:core:designsystem`.

Use:

```kotlin
ColorLinkTheme.colors.primary
ColorLinkTheme.spacing.stackMd
ColorLinkTheme.shapes.large
ColorLinkTheme.typography.headlineMd
```

Do not use hard-coded colors like:

```kotlin
Color.Blue
Color.Red
Color(0xFF2196F3)
```

Exception: token declarations inside the design system module.

## Layout rules

General screen rules:

- Respect safe drawing areas.
- Use `containerPadding` for main content.
- Use a max content width for large screens.
- Use responsive layout for landscape.
- Avoid cramped vertical spacing.
- Keep gameplay canvas visually prioritized.

Gameplay screen rules:

- Board must be square.
- Board must not be clipped by controls.
- Top app bar contains back, level title, and move count.
- Controls stay below the board.
- Hide bottom nav on gameplay.

## Game board rendering

Recommended approach:

- Use a `BoxWithConstraints` to calculate cell size.
- Use `Canvas` for paths for smooth continuous lines.
- Use composables for dots if easier to animate and provide semantics.
- Use a transparent grid overlay or subtle cell backgrounds.

Board structure:

```kotlin
@Composable
fun GameBoard(
    level: Level,
    paths: List<ColorPath>,
    activePath: ColorPath?,
    modifier: Modifier = Modifier,
    onStartDrag: (BoardPosition) -> Unit,
    onDragTo: (BoardPosition) -> Unit,
    onEndDrag: () -> Unit
)
```

Do not hard-code 6x6. Use `level.rows` and `level.columns`.

## Pointer input rules

Use `pointerInput` for drag handling.

High-level behavior:

```kotlin
Modifier.pointerInput(level.id, paths, activePath) {
    detectDragGestures(
        onDragStart = { offset ->
            offset.toBoardPositionOrNull()?.let(onStartDrag)
        },
        onDrag = { change, _ ->
            change.position.toBoardPositionOrNull()?.let(onDragTo)
        },
        onDragEnd = onEndDrag,
        onDragCancel = onEndDrag
    )
}
```

Rules:

- Convert offsets to board positions inside UI.
- Validate moves inside ViewModel/domain UseCases.
- Avoid emitting duplicate `DragTo` intents for the same cell.
- Support fast drag by letting ViewModel/domain interpolate skipped cells if needed.

## Offset to board position

Use board size and cell count:

```kotlin
private fun Offset.toBoardPositionOrNull(
    boardSizePx: Float,
    rows: Int,
    columns: Int
): BoardPosition? {
    if (x !in 0f..boardSizePx || y !in 0f..boardSizePx) return null

    val column = (x / (boardSizePx / columns)).toInt().coerceIn(0, columns - 1)
    val row = (y / (boardSizePx / rows)).toInt().coerceIn(0, rows - 1)

    return BoardPosition(row = row, column = column)
}
```

Account for padding and grid gaps if the board uses internal padding.

## Path drawing

Use rounded stroke caps and joins.

```kotlin
drawPath(
    path = path,
    color = color,
    style = Stroke(
        width = pathWidthPx,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
    )
)
```

For glow:

- Draw a wider translucent path behind the main path.
- Draw the solid path above it.
- Keep glow subtle to avoid visual noise.

## Dot rendering

Dot node behavior:

- Size: 55–65% of cell size.
- Shape: circle.
- Use matching color and glow.
- Animate scale for active/current endpoint.
- Avoid excessive infinite animations for all dots.

Example:

```kotlin
@Composable
fun DotNode(
    color: DotColor,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.10f else 1.0f,
        label = "dot-scale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(color.toComposeColor(), CircleShape)
    )
}
```

## Animation rules

Use Compose animation APIs:

- `animateFloatAsState`
- `animateColorAsState`
- `updateTransition`
- `Animatable`
- `AnimatedVisibility`

Keep animation durations aligned with the design system.

Do not create infinite animations for large lists unless they are lightweight.

## Screen-level animations

Use subtle transitions:

- fade in
- small vertical slide
- scale for cards/buttons
- board reveal on gameplay load

Avoid heavy particle systems in MVP. Win celebration can start with:

- board glow pulse
- success scale bounce
- confetti placeholder animation

## Component naming

Composable names use PascalCase.

Examples:

```kotlin
ColorLinkScaffold
ColorLinkTopAppBar
ColorLinkBottomNavBar
GlassCard
PrimaryGlowButton
LevelCard
GameBoard
DotNode
PathCanvas
GameControls
SettingsRow
```

## Preview rules

Add previews for:

- Home screen
- Level selection locked/unlocked/completed states
- Gameplay empty state
- Gameplay partial progress state
- Gameplay completed state
- Settings screen

Preview functions should not require real ViewModels or Koin.

Use fake state objects.

## Accessibility rules

- Every button requires a meaningful content description.
- Board cells do not need individual content descriptions unless accessibility gameplay mode is implemented.
- Provide descriptions for controls: Undo, Hint, Restart, Back, Settings.
- Tappable targets must be at least 48dp.
- Text should not be clipped at larger font scale.

## Performance rules

- Keep board state immutable but avoid excessive object creation during pointer movement.
- Deduplicate repeated drag cells before sending intents.
- Use `remember` for derived UI calculations where safe.
- Use `derivedStateOf` for derived render-only state.
- Keep Canvas drawing inside a single board composable.
- Do not recompose the entire app on every drag if only board state changes.

## Do not do this

Do not put validation in composables:

```kotlin
if (targetCell.isOccupied) return
```

Instead:

```kotlin
onIntent(GameplayIntent.DragTo(targetCell))
```

The ViewModel/domain layer decides if the move is valid.

Do not call navigation from reusable components:

```kotlin
navController.navigate(...)
```

Instead emit events from the ViewModel or pass a clear callback at the route level.

## Recommended UI state mapping

For complex visual data, convert domain to UI models at the feature boundary.

```kotlin
data class BoardUiState(
    val rows: Int,
    val columns: Int,
    val dots: List<DotUiModel>,
    val paths: List<PathUiModel>,
    val activeColor: DotColor?
)
```

Keep UI models simple and immutable.
