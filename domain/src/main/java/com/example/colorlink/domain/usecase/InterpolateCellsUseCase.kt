package com.example.colorlink.domain.usecase

import com.example.colorlink.domain.model.BoardPosition
import kotlin.math.abs
import kotlin.math.sign

class InterpolateCellsUseCase {
    operator fun invoke(start: BoardPosition, end: BoardPosition): List<BoardPosition> {
        val cells = mutableListOf<BoardPosition>()
        val rowDiff = end.row - start.row
        val colDiff = end.column - start.column

        if (rowDiff != 0 && colDiff != 0) {
            val stepCol = colDiff.sign
            for (c in 1..abs(colDiff)) {
                cells.add(BoardPosition(start.row, start.column + c * stepCol))
            }
            val stepRow = rowDiff.sign
            for (r in 1..abs(rowDiff)) {
                cells.add(BoardPosition(start.row + r * stepRow, end.column))
            }
        } else if (rowDiff != 0) {
            val step = rowDiff.sign
            for (i in 1..abs(rowDiff)) {
                cells.add(BoardPosition(start.row + i * step, start.column))
            }
        } else if (colDiff != 0) {
            val step = colDiff.sign
            for (i in 1..abs(colDiff)) {
                cells.add(BoardPosition(start.row, start.column + i * step))
            }
        }
        return cells
    }
}
