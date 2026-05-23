package com.example.colorlink.domain.repository

import com.example.colorlink.domain.model.Level

interface LevelRepository {
    suspend fun getLevel(levelId: String): Level
    suspend fun getLevelsByPack(packId: String): List<Level>
}
