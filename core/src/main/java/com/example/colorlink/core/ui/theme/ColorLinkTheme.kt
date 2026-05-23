package com.example.colorlink.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun ColorLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Only Dark Palette is supported for now as per design-system.md
    val colors = DarkColorPalette
    val typography = colorLinkTypography()
    val spacing = colorLinkSpacing()

    CompositionLocalProvider(
        LocalColorLinkColors provides colors,
        LocalColorLinkTypography provides typography,
        LocalColorLinkSpacing provides spacing,
        LocalColorLinkShapes provides ColorLinkShapes(),
        content = content
    )
}

object ColorLinkTheme {
    val colors: ColorLinkColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColorLinkColors.current

    val typography: ColorLinkTypography
        @Composable
        get() = LocalColorLinkTypography.current ?: colorLinkTypography()

    val spacing: ColorLinkSpacing
        @Composable
        get() = LocalColorLinkSpacing.current ?: colorLinkSpacing()

    val shapes: ColorLinkShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalColorLinkShapes.current
}
