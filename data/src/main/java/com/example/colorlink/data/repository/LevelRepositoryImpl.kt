package com.example.colorlink.data.repository

import android.content.Context
import com.example.colorlink.data.mapper.toDomain
import com.example.colorlink.data.model.LevelPackDto
import com.example.colorlink.domain.model.Level
import com.example.colorlink.domain.repository.LevelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LevelRepositoryImpl(
    private val context: Context,
    private val json: Json
) : LevelRepository {

    override suspend fun getLevel(levelId: String): Level = withContext(Dispatchers.IO) {
        // In a real app, we might scan all packs, but for MVP we assume we know the pack 
        // or search through them. For now, let's just load beginner.
        val levels = getLevelsByPack("beginner")
        levels.find { it.id == levelId } ?: throw NoSuchElementException("Level $levelId not found")
    }

    override suspend fun getLevelsByPack(packId: String): List<Level> = withContext(Dispatchers.IO) {
        val fileName = "levels/$packId.json"
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val packDto = json.decodeFromString<LevelPackDto>(jsonString)
        packDto.levels.map { it.toDomain() }
    }
}
