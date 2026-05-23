package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.Level

class GetHintUseCase {
    operator fun invoke(level: Level, currentPaths: List<ColorPath>): ColorPath? {
        // Find a solution path where the user hasn't completed it correctly yet
        return level.solutions.find { solPath ->
            val userPath = currentPaths.find { it.color == solPath.color }
            userPath == null || !userPath.isCompleted || userPath.cells != solPath.cells
        }
    }
}
