package com.example.colorlink.data.mapper

import com.example.colorlink.data.model.*
import com.example.colorlink.domain.model.DotColor
import org.junit.Assert.assertEquals
import org.junit.Test

class LevelMapperTest {

    @Test
    fun `map LevelDto to Domain Level`() {
        val dto = LevelDto(
            id = "test_001",
            number = 1,
            difficulty = "BEGINNER",
            gridSize = GridSizeDto(5, 5),
            pairs = listOf(
                DotPairDto(
                    id = "pair_1",
                    color = "Red",
                    start = PositionDto(0, 0),
                    end = PositionDto(4, 4)
                )
            ),
            starRules = StarRulesDto(10, 20)
        )

        val domain = dto.toDomain()

        assertEquals("test_001", domain.id)
        assertEquals(5, domain.rows)
        assertEquals(5, domain.columns)
        assertEquals(2, domain.dots.size)
        assertEquals(DotColor.Red, domain.dots[0].color)
        assertEquals(0, domain.dots[0].position.row)
        assertEquals(0, domain.dots[0].position.column)
        assertEquals(10, domain.starRules?.threeStarsMaxMoves)
    }
}
