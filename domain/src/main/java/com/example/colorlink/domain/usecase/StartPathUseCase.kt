package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.*

class StartPathUseCase {
    operator fun invoke(
        level: Level,
        existingPaths: List<ColorPath>,
        startPosition: BoardPosition
    ): Pair<List<ColorPath>, ColorPath?> {
        val dot = level.dots.find { it.position == startPosition }
        
        if (dot != null) {
            // Rule: Starting from a dot clears the previous path of that color
            val filteredPaths = existingPaths.filter { it.color != dot.color }
            val newActivePath = ColorPath(color = dot.color, cells = listOf(startPosition))
            return filteredPaths to newActivePath
        }

        // Resume incomplete path if touching its endpoint
        val resumePath = existingPaths.find { path ->
            !path.isCompleted && path.cells.last() == startPosition
        }
        
        if (resumePath != null) {
            val filteredPaths = existingPaths.filter { it.color != resumePath.color }
            return filteredPaths to resumePath
        }

        return existingPaths to null
    }
}
