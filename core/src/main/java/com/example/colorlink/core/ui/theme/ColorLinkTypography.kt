package com.example.colorlink.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
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

@Composable
fun colorLinkTypography(): ColorLinkTypography {
    return ColorLinkTypography(
        displayLg = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.Bold,
            fontSize = 33.ssp(),
            lineHeight = 40.ssp(),
            letterSpacing = (-0.02).ssp()
        ),
        headlineLg = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.Bold,
            fontSize = 27.ssp(),
            lineHeight = 33.ssp()
        ),
        headlineMd = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.ssp(),
            lineHeight = 27.ssp()
        ),
        bodyLg = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.Medium,
            fontSize = 15.ssp(),
            lineHeight = 22.ssp()
        ),
        bodyMd = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.Normal,
            fontSize = 13.ssp(),
            lineHeight = 20.ssp()
        ),
        labelLg = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.ssp(),
            lineHeight = 17.ssp(),
            letterSpacing = 0.05.ssp()
        ),
        labelSm = TextStyle(
            fontFamily = Quicksand,
            fontWeight = FontWeight.Medium,
            fontSize = 10.ssp(),
            lineHeight = 13.ssp()
        )
    )
}

@Composable
fun Double.ssp(): TextUnit {
    return (this.toFloat() * 1.ssp().value).sp
}

val LocalColorLinkTypography = staticCompositionLocalOf<ColorLinkTypography?> { null }
