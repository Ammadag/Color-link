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

    CompositionLocalProvider(
        LocalColorLinkColors provides colors,
        LocalColorLinkTypography provides ColorLinkTypographyDefault,
        LocalColorLinkSpacing provides ColorLinkSpacing(),
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
        @ReadOnlyComposable
        get() = LocalColorLinkTypography.current

    val spacing: ColorLinkSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalColorLinkSpacing.current

    val shapes: ColorLinkShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalColorLinkShapes.current
}
