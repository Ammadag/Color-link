package com.example.colorlink.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.colorlink.domain.model.LevelProgress
import com.example.colorlink.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
internal data class ProgressMapDto(
    val progress: Map<String, LevelProgressDto> = emptyMap()
)

@Serializable
internal data class LevelProgressDto(
    val levelId: String,
    val isCompleted: Boolean,
    val bestMoves: Int?,
    val stars: Int,
    val completedAtMillis: Long
)

class ProgressRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : ProgressRepository {

    private val progressKey = stringPreferencesKey("level_progress")

    override suspend fun saveLevelProgress(progress: LevelProgress) {
        dataStore.edit { preferences ->
            val currentJson = preferences[progressKey] ?: "{}"
            val currentMap = try {
                json.decodeFromString<ProgressMapDto>(currentJson)
            } catch (e: Exception) {
                ProgressMapDto()
            }
            
            val updatedMap = currentMap.progress.toMutableMap().apply {
                put(progress.levelId, progress.toDto())
            }
            
            preferences[progressKey] = json.encodeToString(ProgressMapDto(updatedMap))
        }
    }

    override suspend fun getLevelProgress(levelId: String): LevelProgress? {
        val progressMap = getAllProgressMap()
        return progressMap[levelId]?.toDomain()
    }

    override suspend fun getAllProgress(): List<LevelProgress> {
        return getAllProgressMap().values.map { it.toDomain() }
    }

    private suspend fun getAllProgressMap(): Map<String, LevelProgressDto> {
        return dataStore.data.map { preferences ->
            val currentJson = preferences[progressKey] ?: "{}"
            try {
                json.decodeFromString<ProgressMapDto>(currentJson).progress
            } catch (e: Exception) {
                emptyMap()
            }
        }.first()
    }

    private fun LevelProgress.toDto() = LevelProgressDto(
        levelId = levelId,
        isCompleted = isCompleted,
        bestMoves = bestMoves,
        stars = stars,
        completedAtMillis = completedAtMillis
    )

    private fun LevelProgressDto.toDomain() = LevelProgress(
        levelId = levelId,
        isCompleted = isCompleted,
        bestMoves = bestMoves,
        stars = stars,
        completedAtMillis = completedAtMillis
    )
}
