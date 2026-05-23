package com.example.colorlink.domain.model

sealed interface CellOccupancy {
    data object Empty : CellOccupancy
    data class DotOccupancy(val color: DotColor) : CellOccupancy
    data class PathOccupancy(val color: DotColor) : CellOccupancy
    data object Blocked : CellOccupancy
}
