package com.example.colorlink.domain.model

data class LevelWithProgress(
    val level: Level,
    val progress: LevelProgress?,
    val isUnlocked: Boolean
)
