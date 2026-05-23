package com.example.colorlink.domain.model

data class UserStats(
    val hints: Int,
    val streaks: Int,
    val coins: Int,
    val lastPlayedDate: Long? = null
)
