package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.logic.GameplayValidator
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.Level

class CheckWinConditionUseCase {
    operator fun invoke(level: Level, paths: List<ColorPath>): Boolean {
        return GameplayValidator.checkWinCondition(level, paths)
    }
}
