# Color Link Gameplay Rules

## Game objective

The player must connect all matching colored dot pairs using continuous paths.

A level is complete only when:

1. Every dot pair of the same color is connected.
2. Every non-blocked board cell is filled by exactly one path.
3. No path crosses or overlaps another path.
4. No path passes through a dot of another color.

## Board model

The board is a rectangular grid.

```kotlin
data class BoardPosition(
    val row: Int,
    val column: Int
)
```

Rows and columns are zero-based.

A level defines:

- `rows`
- `columns`
- `dots`
- optional `blockedCells`
- optional `optimalMoves`

For the first release, blocked cells are optional. The game should still be designed so blocked cells can be added later.

## Dot rules

- Every playable color should have exactly two endpoint dots in a standard level.
- Dots are fixed and cannot be moved.
- A path can start only from a dot or from the latest cell of an active same-color path.
- A path can end only on the matching color dot.
- A path cannot pass through a dot of another color.

## Path rules

A path is a list of grid cells.

```kotlin
data class ColorPath(
    val color: DotColor,
    val cells: List<BoardPosition>,
    val isCompleted: Boolean
)
```

Rules:

- Movement is orthogonal only: up, down, left, right.
- Diagonal movement is invalid.
- A path cannot leave the board bounds.
- A path cannot enter a blocked cell.
- A path cannot overlap a different color path.
- A path cannot cross another path.
- A path cannot enter a different color endpoint dot.
- A path may backtrack over its own latest cells while dragging.
- A path cannot branch.
- Only one final path per color should exist.

## Drag interaction

### Start drag

When the user touches the board:

1. Convert pointer position to board cell.
2. If the cell has a colored dot, start or replace the path for that color.
3. If the cell is the latest endpoint of an incomplete existing path, resume that path.
4. Otherwise ignore the touch or emit light invalid feedback.

### Drag to cell

When the pointer moves:

1. Convert pointer position to board cell.
2. Ignore if it is the same as the current last cell.
3. If the cell is adjacent, validate it.
4. If the pointer skips cells due to fast drag, interpolate the intermediate cells and validate in order.
5. If valid, append the cell.
6. If the cell is the matching endpoint dot, mark the path completed.
7. If invalid, keep the previous path and emit invalid feedback.

### End drag

When the user releases:

- Keep valid incomplete paths.
- If the path ended on the matching dot, keep it as completed.
- Check win condition.
- Save progress if level is completed.

## Same-color path replacement

When the user starts again from a dot of a color that already has a path:

- Clear the previous path for that color.
- Keep paths of other colors unchanged.
- Start a new active path from the selected dot.

This behavior makes the game forgiving and expected for Flow-style puzzles.

## Backtracking behavior

If the user drags back to the previous cell in the active path:

- Remove the last cell from the path.
- Continue dragging from the new last cell.

If the user drags to any earlier own-path cell that is not the immediate previous cell:

- Trim the path back to that cell.
- Do not treat this as a crossing.

## Occupancy rules

Each cell may contain only one of these states:

```kotlin
sealed interface CellOccupancy {
    data object Empty : CellOccupancy
    data class Dot(val color: DotColor) : CellOccupancy
    data class Path(val color: DotColor) : CellOccupancy
    data object Blocked : CellOccupancy
}
```

A dot cell is considered occupied by its own color.

## Validation algorithm

Use domain-level validation.

Pseudo logic:

```text
validateMove(level, existingPaths, activePath, targetCell): MoveResult

1. If targetCell is outside board -> Invalid.OutOfBounds
2. If targetCell is blocked -> Invalid.BlockedCell
3. If targetCell is not adjacent to activePath.lastCell -> Invalid.NotAdjacent
4. If targetCell is previous own-path cell -> Valid.Backtrack
5. If targetCell is earlier own-path cell -> Valid.TrimToCell
6. If targetCell contains other color path -> Invalid.OccupiedByOtherPath
7. If targetCell contains other color dot -> Invalid.OtherColorDot
8. If targetCell contains same color dot and it is not the start dot -> Valid.CompletePath
9. If targetCell is empty -> Valid.Append
```

Return typed results instead of booleans.

Example:

```kotlin
sealed interface MoveResult {
    data class Valid(val updatedPath: ColorPath) : MoveResult
    data class Completed(val completedPath: ColorPath) : MoveResult
    data class Backtracked(val updatedPath: ColorPath) : MoveResult
    data class Invalid(val reason: InvalidMoveReason) : MoveResult
}

enum class InvalidMoveReason {
    OutOfBounds,
    NotAdjacent,
    BlockedCell,
    OccupiedByOtherPath,
    OtherColorDot,
    AlreadyCompleted,
    NoActivePath
}
```

## Win condition

A level is won when:

```text
all color pairs are completed
AND every playable cell is occupied by a dot or path
AND there are no invalid overlaps
```

Do not mark a level complete simply because all colored pairs are connected. The board must be fully filled.

## Move count

Move count can be calculated in one of two ways.

Recommended for first release:

- Increment move count when the player completes or modifies a path drag.
- Do not increment for invalid drag attempts.
- Do not increment for opening menus/settings.

Alternative later:

- Count every added segment.

Whichever approach is used, keep it consistent across levels and scoring.

## Scoring and stars

Recommended star logic:

```text
3 stars: moveCount <= optimalMoves
2 stars: moveCount <= optimalMoves + 3
1 star: level completed
```

If `optimalMoves` is missing:

```text
3 stars: completed with no restart and no hint
2 stars: completed with either restart or hint
1 star: completed
```

## Hints

Hint behavior:

- A hint should reveal the next valid segment or highlight the next color to solve.
- A hint should not auto-complete the whole level unless explicitly designed as a paid/reward hint.
- Decrease hint count only when a hint is successfully used.
- If no hints remain, emit a `ShowError` or open a reward flow later.

## Undo

Undo should reverse the most recent meaningful gameplay action.

Minimum MVP:

- Undo removes the latest active or completed path change.

Better version:

- Maintain a stack of `GameSnapshot` values.
- Push snapshot after each completed drag action.
- Pop on undo.

Example:

```kotlin
data class GameSnapshot(
    val paths: List<ColorPath>,
    val moveCount: Int,
    val hintCount: Int
)
```

## Restart

Restart resets:

- all paths
- active path
- selected color
- move count
- level completion state

Restart should not reset:

- total hint count unless hints were consumed and product design says otherwise
- unlocked levels
- best score

## Level progress persistence

Persist after level completion:

```kotlin
data class LevelProgress(
    val levelId: String,
    val isCompleted: Boolean,
    val bestMoves: Int?,
    val stars: Int,
    val completedAtMillis: Long
)
```

Also persist:

- last played level
- unlocked level count
- daily puzzle completion

## Edge cases

The implementation must handle:

- Fast drag skipping cells
- Dragging outside the board and back in
- Replacing an existing completed path
- Backtracking a completed path after restart/reselect
- Invalid drag into another path
- Invalid drag through another color dot
- Completing paths while empty cells remain
- Different board sizes such as 5x5, 6x6, 7x7, 8x8

## Recommended first level sizes

- Onboarding/tutorial: 5x5
- Early levels: 5x5 and 6x6
- Medium levels: 7x7
- Advanced levels: 8x8 or larger

Do not hard-code board rendering to 6x6.
