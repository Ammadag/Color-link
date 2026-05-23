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
        // Search through all packs
        val packs = listOf("classic", "easy", "medium", "hard")
        val levels = packs.flatMap { getLevelsByPack(it) }

        val level = if (levelId.startsWith("level_")) {
            val number = levelId.removePrefix("level_").toIntOrNull()
            levels.find { it.number == number }
        } else {
            levels.find { it.id == levelId }
        }

        level ?: throw NoSuchElementException("Level $levelId not found")
    }

    override suspend fun getLevelsByPack(packId: String): List<Level> =
        withContext(Dispatchers.IO) {
            val fileName = "levels/$packId.json"
            try {
                val jsonString =
                    context.assets.open(fileName).bufferedReader().use { it.readText() }
                val packDto = json.decodeFromString<LevelPackDto>(jsonString)
                packDto.levels.map { it.toDomain() }
            } catch (e: Exception) {
                // If it fails, return empty list instead of crashing/hanging
                emptyList()
            }
        }
}
