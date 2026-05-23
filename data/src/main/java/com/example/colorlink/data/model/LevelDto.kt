package com.example.colorlink.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LevelPackDto(
    val packId: String,
    val title: String? = null,
    val description: String? = null,
    val levels: List<LevelDto>
)

@Serializable
data class LevelDto(
    val id: String,
    val number: Int,
    val difficulty: String,
    val gridSize: GridSizeDto,
    val pairs: List<DotPairDto>,
    val starRules: StarRulesDto? = null,
    val solution: List<SolutionPathDto>? = null
)

@Serializable
data class SolutionPathDto(
    val pairId: String,
    val path: List<PositionDto>
)

@Serializable
data class GridSizeDto(
    val rows: Int,
    val columns: Int
)

@Serializable
data class DotPairDto(
    val id: String,
    val color: String,
    val start: PositionDto,
    val end: PositionDto
)

@Serializable
data class PositionDto(
    val row: Int,
    val col: Int
)

@Serializable
data class StarRulesDto(
    val threeStarsMaxMoves: Int,
    val twoStarsMaxMoves: Int
)
