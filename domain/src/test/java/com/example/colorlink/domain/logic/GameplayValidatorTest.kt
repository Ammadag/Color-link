package com.example.colorlink.domain.logic

import com.example.colorlink.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class GameplayValidatorTest {

    private val level5x5 = Level(
        id = "test_5x5",
        number = 1,
        rows = 5,
        columns = 5,
        dots = listOf(
            Dot("red_start", DotColor.Red, BoardPosition(0, 0)),
            Dot("red_end", DotColor.Red, BoardPosition(0, 4)),
            Dot("blue_start", DotColor.Blue, BoardPosition(1, 0)),
            Dot("blue_end", DotColor.Blue, BoardPosition(1, 4))
        )
    )

    @Test
    fun `valid move to adjacent cell`() {
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0)))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(0, 1)
        )

        assertTrue(result is MoveResult.Valid)
        val updatedPath = (result as MoveResult.Valid).updatedPath
        assertEquals(2, updatedPath.cells.size)
        assertEquals(BoardPosition(0, 1), updatedPath.cells.last())
    }

    @Test
    fun `diagonal move is invalid`() {
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0)))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(1, 1)
        )

        assertTrue(result is MoveResult.Invalid)
        assertEquals(InvalidMoveReason.NotAdjacent, (result as MoveResult.Invalid).reason)
    }

    @Test
    fun `move outside board is invalid`() {
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0)))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(-1, 0)
        )

        assertTrue(result is MoveResult.Invalid)
        assertEquals(InvalidMoveReason.OutOfBounds, (result as MoveResult.Invalid).reason)
    }

    @Test
    fun `crossing another color path is invalid`() {
        val otherPath = ColorPath(DotColor.Blue, listOf(BoardPosition(1, 0), BoardPosition(1, 1)))
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 1)))
        
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = listOf(otherPath),
            activePath = activePath,
            targetCell = BoardPosition(1, 1)
        )

        assertTrue(result is MoveResult.Invalid)
        assertEquals(InvalidMoveReason.OccupiedByOtherPath, (result as MoveResult.Invalid).reason)
    }

    @Test
    fun `entering different color dot is invalid`() {
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0)))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(1, 0) // Blue dot position
        )

        assertTrue(result is MoveResult.Invalid)
        assertEquals(InvalidMoveReason.OtherColorDot, (result as MoveResult.Invalid).reason)
    }

    @Test
    fun `backtracking works`() {
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0), BoardPosition(0, 1)))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(0, 0)
        )

        assertTrue(result is MoveResult.Backtracked)
        val updatedPath = (result as MoveResult.Backtracked).updatedPath
        assertEquals(1, updatedPath.cells.size)
        assertEquals(BoardPosition(0, 0), updatedPath.cells.last())
    }

    @Test
    fun `path trimming works`() {
        val activePath = ColorPath(DotColor.Red, listOf(
            BoardPosition(0, 0), 
            BoardPosition(0, 1), 
            BoardPosition(0, 2), 
            BoardPosition(1, 2)
        ))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(0, 1)
        )

        assertTrue(result is MoveResult.Backtracked)
        val updatedPath = (result as MoveResult.Backtracked).updatedPath
        assertEquals(2, updatedPath.cells.size)
        assertEquals(BoardPosition(0, 1), updatedPath.cells.last())
    }

    @Test
    fun `complete pair detection works`() {
        val activePath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0), BoardPosition(0, 1), BoardPosition(0, 2), BoardPosition(0, 3)))
        val result = GameplayValidator.validateMove(
            level = level5x5,
            existingPaths = emptyList(),
            activePath = activePath,
            targetCell = BoardPosition(0, 4) // red_end position
        )

        assertTrue(result is MoveResult.Completed)
        val completedPath = (result as MoveResult.Completed).completedPath
        assertTrue(completedPath.isCompleted)
        assertEquals(BoardPosition(0, 4), completedPath.cells.last())
    }

    @Test
    fun `win condition requires all pairs connected`() {
        val level = Level(
            id = "tiny", number = 1, rows = 2, columns = 2,
            dots = listOf(
                Dot("r1", DotColor.Red, BoardPosition(0, 0)),
                Dot("r2", DotColor.Red, BoardPosition(0, 1)),
                Dot("b1", DotColor.Blue, BoardPosition(1, 0)),
                Dot("b2", DotColor.Blue, BoardPosition(1, 1))
            )
        )
        
        val redPath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0), BoardPosition(0, 1)), isCompleted = true)
        val bluePath = ColorPath(DotColor.Blue, listOf(BoardPosition(1, 0)), isCompleted = false)

        assertFalse(GameplayValidator.checkWinCondition(level, listOf(redPath, bluePath)))
    }

    @Test
    fun `win condition requires all playable cells filled`() {
        val level = Level(
            id = "small", number = 1, rows = 3, columns = 2,
            dots = listOf(
                Dot("r1", DotColor.Red, BoardPosition(0, 0)),
                Dot("r2", DotColor.Red, BoardPosition(2, 0)),
                Dot("b1", DotColor.Blue, BoardPosition(0, 1)),
                Dot("b2", DotColor.Blue, BoardPosition(2, 1))
            )
        )
        // Paths connect dots but leave center row (1,0 and 1,1) empty? 
        // No, let's make it more explicit.
        
        val redPath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0), BoardPosition(2, 0)), isCompleted = true) // Illegal jump but for testing logic
        val bluePath = ColorPath(DotColor.Blue, listOf(BoardPosition(0, 1), BoardPosition(2, 1)), isCompleted = true)

        // Missing (1,0) and (1,1)
        assertFalse(GameplayValidator.checkWinCondition(level, listOf(redPath, bluePath)))

        val fullRedPath = ColorPath(DotColor.Red, listOf(BoardPosition(0, 0), BoardPosition(1, 0), BoardPosition(2, 0)), isCompleted = true)
        val fullBluePath = ColorPath(DotColor.Blue, listOf(BoardPosition(0, 1), BoardPosition(1, 1), BoardPosition(2, 1)), isCompleted = true)

        assertTrue(GameplayValidator.checkWinCondition(level, listOf(fullRedPath, fullBluePath)))
    }
}
