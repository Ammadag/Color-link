package com.example.colorlink.domain.repository

import com.example.colorlink.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserStatsRepository {
    fun getUserStats(): Flow<UserStats>
    suspend fun updateHints(hints: Int)
    suspend fun updateStreaks(streaks: Int)
    suspend fun updateCoins(coins: Int)
    suspend fun updateLastPlayedDate(timestamp: Long)
}
