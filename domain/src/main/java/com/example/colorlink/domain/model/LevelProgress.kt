package com.example.colorlink.domain.model

data class LevelProgress(
    val levelId: String,
    val isCompleted: Boolean,
    val bestMoves: Int?,
    val stars: Int,
    val completedAtMillis: Long
)
