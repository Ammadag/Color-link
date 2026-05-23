package com.example.colorlink.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ColorLinkSpacing(
    val unit: Dp = 4.dp,
    val stackSm: Dp = 8.dp,
    val stackMd: Dp = 16.dp,
    val gutter: Dp = 16.dp,
    val containerPadding: Dp = 24.dp,
    val stackLg: Dp = 32.dp
)

@Composable
fun colorLinkSpacing(): ColorLinkSpacing {
    return ColorLinkSpacing(
        unit = 3.sdp(),
        stackSm = 7.sdp(),
        stackMd = 13.sdp(),
        gutter = 13.sdp(),
        containerPadding = 20.sdp(),
        stackLg = 27.sdp()
    )
}

val LocalColorLinkSpacing = staticCompositionLocalOf<ColorLinkSpacing?> { null }


