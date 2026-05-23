package com.example.colorlink.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Note: Quicksand font files are not yet present in the project.
// Reverting to FontFamily.Default to fix build errors. 
// Add .ttf files to core/src/main/res/font/ and update this once available.
val Quicksand = FontFamily.Default

@Immutable
data class ColorLinkTypography(
    val displayLg: TextStyle,
    val headlineLg: TextStyle,
    val headlineMd: TextStyle,
    val bodyLg: TextStyle,
    val bodyMd: TextStyle,
    val labelLg: TextStyle,
    val labelSm: TextStyle
)

val ColorLinkTypographyDefault = ColorLinkTypography(
    displayLg = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.02).sp // Adjusted to .sp for consistency with core, but docs suggest em.
    ),
    headlineLg = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMd = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    bodyLg = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    bodyMd = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelLg = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.05.sp
    ),
    labelSm = TextStyle(
        fontFamily = Quicksand,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

val LocalColorLinkTypography = staticCompositionLocalOf { ColorLinkTypographyDefault }
