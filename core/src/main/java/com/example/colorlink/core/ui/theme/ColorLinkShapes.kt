package com.example.colorlink.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class ColorLinkShapes(
    val default: Shape = RoundedCornerShape(16.dp),
    val large: Shape = RoundedCornerShape(32.dp),
    val extraLarge: Shape = RoundedCornerShape(48.dp),
    val full: Shape = RoundedCornerShape(9999.dp)
)

val LocalColorLinkShapes = staticCompositionLocalOf { ColorLinkShapes() }
