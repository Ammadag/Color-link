package com.example.colorlink.domain.repository

import com.example.colorlink.domain.model.LevelProgress

interface GameRepository {
    suspend fun saveLevelProgress(progress: LevelProgress)
    suspend fun getLevelProgress(levelId: String): LevelProgress?
    suspend fun getAllProgress(): List<LevelProgress>
}
