package com.example.colorlink.domain.logic

import com.example.colorlink.domain.model.*
import kotlin.math.abs

object GameplayValidator {

    fun validateMove(
        level: Level,
        existingPaths: List<ColorPath>,
        activePath: ColorPath,
        targetCell: BoardPosition
    ): MoveResult {
        // 1. Out of bounds
        if (targetCell.row !in 0 until level.rows || targetCell.column !in 0 until level.columns) {
            return MoveResult.Invalid(InvalidMoveReason.OutOfBounds)
        }

        // 2. Blocked cell
        if (level.blockedCells.contains(targetCell)) {
            return MoveResult.Invalid(InvalidMoveReason.BlockedCell)
        }

        val lastCell = activePath.cells.lastOrNull() ?: return MoveResult.Invalid(InvalidMoveReason.NoActivePath)

        // Already completed path check
        if (activePath.isCompleted) {
            return MoveResult.Invalid(InvalidMoveReason.AlreadyCompleted)
        }

        // Rule: If targetCell is the same as the current tip, ignore it
        if (lastCell == targetCell) {
            return MoveResult.Invalid(InvalidMoveReason.NotAdjacent)
        }

        // Rule: Backtrack or Trim
        if (activePath.cells.contains(targetCell)) {
            val index = activePath.cells.indexOf(targetCell)
            return MoveResult.Backtracked(activePath.copy(cells = activePath.cells.take(index + 1)))
        }

        // 3. Not adjacent (for new segments)
        if (!isAdjacent(lastCell, targetCell)) {
            return MoveResult.Invalid(InvalidMoveReason.NotAdjacent)
        }

        // Check if targetCell is a dot
        val dotAtTarget = level.dots.find { it.position == targetCell }
        if (dotAtTarget != null) {
            if (dotAtTarget.color == activePath.color) {
                // Same color dot - Complete Path
                // Ensure it's not the START dot
                if (targetCell != activePath.cells.first()) {
                    return MoveResult.Completed(activePath.copy(cells = activePath.cells + targetCell, isCompleted = true))
                } else {
                    return MoveResult.Invalid(InvalidMoveReason.NotAdjacent)
                }
            } else {
                // Other color dot
                return MoveResult.Invalid(InvalidMoveReason.OtherColorDot)
            }
        }

        // Check if occupied by other path (and not a dot)
        // Rule: We can overwrite other incomplete paths to make the game feel fluid
        // For now, let's keep it simple: if it's occupied by a DIFFERENT color path, it's invalid
        val otherPath = existingPaths.find { it.color != activePath.color && it.cells.contains(targetCell) }
        if (otherPath != null) {
            return MoveResult.Invalid(InvalidMoveReason.OccupiedByOtherPath)
        }

        // Valid Append
        return MoveResult.Valid(activePath.copy(cells = activePath.cells + targetCell))
    }

    fun checkWinCondition(
        level: Level,
        paths: List<ColorPath>
    ): Boolean {
        // 1. All pairs connected
        val requiredColors = level.dots.map { it.color }.distinct()
        val allConnected = requiredColors.all { color ->
            paths.any { it.color == color && it.isCompleted }
        }
        if (!allConnected) return false

        // 2. Every playable cell occupied
        val totalCells = level.rows * level.columns
        val blockedCount = level.blockedCells.size
        val playableCellsCount = totalCells - blockedCount

        val occupiedCells = mutableSetOf<BoardPosition>()
        paths.forEach { path ->
            occupiedCells.addAll(path.cells)
        }

        return occupiedCells.size == playableCellsCount
    }

    private fun isAdjacent(p1: BoardPosition, p2: BoardPosition): Boolean {
        return (abs(p1.row - p2.row) == 1 && p1.column == p2.column) ||
                (abs(p1.column - p2.column) == 1 && p1.row == p2.row)
    }
}
