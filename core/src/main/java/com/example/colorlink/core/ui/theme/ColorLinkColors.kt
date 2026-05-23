package com.example.colorlink.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorLinkColors(
    val background: Color,
    val surface: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceBright: Color,
    val surfaceVariant: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val outlineVariant: Color,
    val primary: Color,
    val primaryContainer: Color,
    val onPrimary: Color,
    val onPrimaryContainer: Color,
    val primaryFixed: Color,
    val primaryFixedDim: Color,
    val secondary: Color,
    val secondaryContainer: Color,
    val onSecondary: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val tertiaryContainer: Color,
    val onTertiary: Color,
    val onTertiaryContainer: Color,
    val error: Color,
    val errorContainer: Color,
    val onError: Color,
    val onErrorContainer: Color,
    // Gameplay Colors
    val gameRed: Color,
    val gameBlue: Color,
    val gameCyan: Color,
    val gamePurple: Color,
    val gameGreen: Color,
    val gameYellow: Color
)

val DarkColorPalette = ColorLinkColors(
    background = Color(0xFF0B1326),
    surface = Color(0xFF0B1326),
    surfaceContainerLowest = Color(0xFF060E20),
    surfaceContainerLow = Color(0xFF131B2E),
    surfaceContainer = Color(0xFF171F33),
    surfaceContainerHigh = Color(0xFF222A3D),
    surfaceContainerHighest = Color(0xFF2D3449),
    surfaceBright = Color(0xFF31394D),
    surfaceVariant = Color(0xFF2D3449),
    onBackground = Color(0xFFDAE2FD),
    onSurface = Color(0xFFDAE2FD),
    onSurfaceVariant = Color(0xFFC2C6D6),
    outline = Color(0xFF8C909F),
    outlineVariant = Color(0xFF424754),
    primary = Color(0xFFADC6FF),
    primaryContainer = Color(0xFF4D8EFF),
    onPrimary = Color(0xFF002E6A),
    onPrimaryContainer = Color(0xFF00285D),
    primaryFixed = Color(0xFFD8E2FF),
    primaryFixedDim = Color(0xFFADC6FF),
    secondary = Color(0xFFDDB7FF),
    secondaryContainer = Color(0xFF6F00BE),
    onSecondary = Color(0xFF490080),
    onSecondaryContainer = Color(0xFFD6A9FF),
    tertiary = Color(0xFF4CD7F6),
    tertiaryContainer = Color(0xFF009EB9),
    onTertiary = Color(0xFF003640),
    onTertiaryContainer = Color(0xFF002F38),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    // Gameplay
    gameRed = Color(0xFFFFB4AB),
    gameBlue = Color(0xFFADC6FF),
    gameCyan = Color(0xFF4CD7F6),
    gamePurple = Color(0xFFDDB7FF),
    gameGreen = Color(0xFF7EE7B8),
    gameYellow = Color(0xFFFFE082)
)

val LocalColorLinkColors = staticCompositionLocalOf { DarkColorPalette }
