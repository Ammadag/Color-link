package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.Level

class CalculateStarsUseCase {
    operator fun invoke(level: Level, moves: Int): Int {
        val rules = level.starRules ?: return 1
        return when {
            moves <= rules.threeStarsMaxMoves -> 3
            moves <= rules.twoStarsMaxMoves -> 2
            else -> 1
        }
    }
}
