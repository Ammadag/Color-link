package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.UserStats
import com.example.colorlink.domain.repository.UserStatsRepository
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val repository: UserStatsRepository
) {
    operator fun invoke(): Flow<UserStats> = repository.getUserStats()
}
