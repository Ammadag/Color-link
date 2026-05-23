package com.example.colorlink.domain.model

data class ColorPath(
    val color: DotColor,
    val cells: List<BoardPosition>,
    val isCompleted: Boolean = false
)
