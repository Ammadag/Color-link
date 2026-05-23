package com.example.colorlink.data.mapper

import com.example.colorlink.data.model.*
import com.example.colorlink.domain.model.*

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
        starRules = starRules?.toDomain()
    )
}

fun PositionDto.toDomain(): BoardPosition = BoardPosition(row = row, column = col)

fun StarRulesDto.toDomain(): StarRules = StarRules(
    threeStarsMaxMoves = threeStarsMaxMoves,
    twoStarsMaxMoves = twoStarsMaxMoves
)

fun String.toDotColor(): DotColor = DotColor.valueOf(this)
