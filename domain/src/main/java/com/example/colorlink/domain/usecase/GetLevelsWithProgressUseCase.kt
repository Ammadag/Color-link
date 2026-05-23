package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.LevelWithProgress
import com.example.colorlink.domain.repository.LevelRepository
import com.example.colorlink.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLevelsWithProgressUseCase(
    private val levelRepository: LevelRepository,
    private val progressRepository: ProgressRepository
) {
    operator fun invoke(packId: String = "beginner"): Flow<List<LevelWithProgress>> = flow {
        val levels = levelRepository.getLevelsByPack(packId)
        val allProgress = progressRepository.getAllProgress().associateBy { it.levelId }
        
        // A level is unlocked if it's the first one or if the previous one is completed
        var previousCompleted = true
        val result = levels.mapIndexed { index, level ->
            val progress = allProgress[level.id]
            val isUnlocked = index == 0 || previousCompleted
            previousCompleted = progress?.isCompleted ?: false
            
            LevelWithProgress(
                level = level,
                progress = progress,
                isUnlocked = isUnlocked
            )
        }
        emit(result)
    }
}
