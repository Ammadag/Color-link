package com.example.colorlink.domain.model

data class Level(
    val id: String,
    val number: Int,
    val rows: Int,
    val columns: Int,
    val dots: List<Dot>,
    val blockedCells: List<BoardPosition> = emptyList(),
    val optimalMoves: Int? = null,
    val starRules: StarRules? = null,
    val solutions: List<ColorPath> = emptyList()
)

data class StarRules(
    val threeStarsMaxMoves: Int,
    val twoStarsMaxMoves: Int
)
