package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.repository.ProgressRepository

data class ProgressSummary(
    val totalStars: Int,
    val nextLevelNumber: Int
)

class GetProgressSummaryUseCase(
    private val progressRepository: ProgressRepository
) {
    suspend operator fun invoke(): ProgressSummary {
        val allProgress = progressRepository.getAllProgress()
        val completedLevels = allProgress.filter { it.isCompleted }

        val totalStars = completedLevels.sumOf { it.stars }

        // Find the highest completed level number to determine the next one
        // Handling both "level_X" and "beginner_XXX" formats by extracting digits
        val maxLevelNumber = completedLevels
            .map { it.levelId }
            .mapNotNull { id ->
                id.filter { it.isDigit() }.toIntOrNull()
            }
            .maxOrNull() ?: 0

        return ProgressSummary(
            totalStars = totalStars,
            nextLevelNumber = maxLevelNumber + 1
        )
    }
}
