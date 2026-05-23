package com.example.colorlink.feature.gameplay.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.DotNode
import com.example.colorlink.core.ui.components.toComposeColor
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.ColorPath
import com.example.colorlink.domain.model.Level

@Composable
fun GameBoard(
    level: Level,
    paths: List<ColorPath>,
    activePath: ColorPath?,
    onStartDrag: (BoardPosition) -> Unit,
    onDragTo: (BoardPosition) -> Unit,
    onEndDrag: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = ColorLinkTheme.colors
    val density = LocalDensity.current
    
    // Pre-calculate path colors to avoid @Composable calls inside DrawScope
    val resolvedPaths = paths.map { it.cells to it.color.toComposeColor(colors) }
    val resolvedActivePath = activePath?.let { it.cells to it.color.toComposeColor(colors) }

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .rememberBoardPointerInput(
                rows = level.rows,
                columns = level.columns,
                onStartDrag = onStartDrag,
                onDragTo = onDragTo,
                onEndDrag = onEndDrag
            )
    ) {
        val boardSizePx = constraints.maxWidth.toFloat()
        val cellSize = boardSizePx / level.columns

        // 1. Grid Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawGrid(level.rows, level.columns, cellSize)
        }

        // 2. Paths
        Canvas(modifier = Modifier.fillMaxSize()) {
            resolvedPaths.forEach { (cells, color) ->
                drawNeonPath(cells, color, cellSize)
            }
            resolvedActivePath?.let { (cells, color) ->
                drawNeonPath(cells, color, cellSize)
            }
        }

        // 3. Dots
        level.dots.forEach { dot ->
            val isActive = activePath?.color == dot.color
            
            val xOffset = with(density) { (dot.position.column * cellSize).toDp() }
            val yOffset = with(density) { (dot.position.row * cellSize).toDp() }
            val cellSizeDp = with(density) { cellSize.toDp() }

            Box(
                modifier = Modifier
                    .offset(x = xOffset, y = yOffset)
                    .size(cellSizeDp),
                contentAlignment = Alignment.Center
            ) {
                DotNode(
                    color = dot.color,
                    isActive = isActive,
                    dotSize = cellSizeDp * 0.6f
                )
            }
        }
    }
}

private fun Modifier.rememberBoardPointerInput(
    rows: Int,
    columns: Int,
    onStartDrag: (BoardPosition) -> Unit,
    onDragTo: (BoardPosition) -> Unit,
    onEndDrag: () -> Unit
): Modifier = pointerInput(rows, columns) {
    val boardSizePx = size.width.toFloat()
    
    detectDragGestures(
        onDragStart = { offset ->
            offset.toBoardPositionOrNull(boardSizePx, rows, columns)?.let(onStartDrag)
        },
        onDrag = { change, _ ->
            change.position.toBoardPositionOrNull(boardSizePx, rows, columns)?.let(onDragTo)
        },
        onDragEnd = onEndDrag,
        onDragCancel = onEndDrag
    )
}

private fun Offset.toBoardPositionOrNull(
    boardSizePx: Float,
    rows: Int,
    columns: Int
): BoardPosition? {
    if (x !in 0f..boardSizePx || y !in 0f..boardSizePx) return null

    val col = (x / (boardSizePx / columns)).toInt().coerceIn(0, columns - 1)
    val row = (y / (boardSizePx / rows)).toInt().coerceIn(0, rows - 1)

    return BoardPosition(row = row, column = col)
}

private fun DrawScope.drawGrid(rows: Int, columns: Int, cellSize: Float) {
    val color = Color.White.copy(alpha = 0.05f)
    val strokeWidth = 1.dp.toPx()

    for (i in 0..rows) {
        drawLine(
            color = color,
            start = Offset(0f, i * cellSize),
            end = Offset(size.width, i * cellSize),
            strokeWidth = strokeWidth
        )
    }
    for (i in 0..columns) {
        drawLine(
            color = color,
            start = Offset(i * cellSize, 0f),
            end = Offset(i * cellSize, size.height),
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawNeonPath(
    cells: List<BoardPosition>,
    color: Color,
    cellSize: Float
) {
    if (cells.size < 2) return

    val path = Path().apply {
        val first = cells.first().toOffset(cellSize)
        moveTo(first.x, first.y)
        cells.drop(1).forEach {
            val next = it.toOffset(cellSize)
            lineTo(next.x, next.y)
        }
    }

    // Glow
    drawPath(
        path = path,
        color = color.copy(alpha = 0.3f),
        style = Stroke(
            width = cellSize * 0.35f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Core
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = cellSize * 0.22f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun BoardPosition.toOffset(cellSize: Float): Offset {
    return Offset(
        x = (column + 0.5f) * cellSize,
        y = (row + 0.5f) * cellSize
    )
}
