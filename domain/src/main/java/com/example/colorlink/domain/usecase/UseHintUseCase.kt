package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.first

class UseHintUseCase(
    private val repository: UserStatsRepository
) {
    suspend operator fun invoke(): Boolean {
        val stats = repository.getUserStats().first()
        if (stats.hints > 0) {
            repository.updateHints(stats.hints - 1)
            return true
        }
        return false
    }
}
