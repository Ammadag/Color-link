package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.logic.GameplayValidator
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.Level
import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.MoveResult

class ValidateMoveUseCase {
    operator fun invoke(
        level: Level,
        existingPaths: List<ColorPath>,
        activePath: ColorPath,
        targetCell: BoardPosition
    ): MoveResult {
        return GameplayValidator.validateMove(level, existingPaths, activePath, targetCell)
    }
}
