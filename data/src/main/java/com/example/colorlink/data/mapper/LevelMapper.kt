package com.example.colorlink.data.mapper

import com.example.colorlink.data.model.LevelDto
import com.example.colorlink.data.model.PositionDto
import com.example.colorlink.data.model.StarRulesDto
import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.Dot
import com.example.colorlink.domain.model.DotColor
import com.example.colorlink.domain.model.Level
import com.example.colorlink.domain.model.StarRules

fun LevelDto.toDomain(): Level {
    return Level(
        id = id,
        number = number,
        rows = gridSize.rows,
        columns = gridSize.columns,
        dots = pairs.flatMap { pair ->
            listOf(
                Dot(
                    id = "${pair.id}_start",
                    color = pair.color.toDotColor(),
                    position = pair.start.toDomain()
                ),
                Dot(
                    id = "${pair.id}_end",
                    color = pair.color.toDotColor(),
                    position = pair.end.toDomain()
                )
            )
        },
        starRules = starRules?.toDomain(),
        solutions = solution?.map { solDto ->
            // Find the color from pairs
            val pair = pairs.find { it.id == solDto.pairId }
            ColorPath(
                color = pair?.color?.toDotColor() ?: DotColor.Blue,
                cells = solDto.path.map { it.toDomain() },
                isCompleted = true
            )
        } ?: emptyList()
    )
}

fun PositionDto.toDomain(): BoardPosition = BoardPosition(row = row, column = col)

fun StarRulesDto.toDomain(): StarRules = StarRules(
    threeStarsMaxMoves = threeStarsMaxMoves,
    twoStarsMaxMoves = twoStarsMaxMoves
)

fun String.toDotColor(): DotColor = try {
    DotColor.valueOf(this.lowercase().replaceFirstChar { it.uppercase() })
} catch (e: Exception) {
    DotColor.Blue // Fallback
}
