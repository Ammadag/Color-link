package com.example.colorlink.domain.model

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
