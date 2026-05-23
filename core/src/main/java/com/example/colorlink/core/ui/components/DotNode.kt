package com.example.colorlink.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.theme.ColorLinkColors
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.domain.model.DotColor

@Composable
fun DotNode(
    color: DotColor,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    dotSize: Dp = 32.dp
) {
    val colors = ColorLinkTheme.colors
    val composeColor = color.toComposeColor(colors)
    
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.12f else 1.0f,
        label = "dot-scale"
    )

    Box(
        modifier = modifier
            .size(dotSize)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .fillMaxSize(1.4f)
                .blur(8.dp)
                .background(composeColor.copy(alpha = 0.4f), CircleShape)
        )
        
        // Main dot
        Box(
            modifier = Modifier
                .size(dotSize)
                .background(composeColor, CircleShape)
        )
    }
}

fun DotColor.toComposeColor(colors: ColorLinkColors): Color {
    return when (this) {
        DotColor.Red -> colors.gameRed
        DotColor.Blue -> colors.gameBlue
        DotColor.Cyan -> colors.gameCyan
        DotColor.Purple -> colors.gamePurple
        DotColor.Green -> colors.gameGreen
        DotColor.Yellow -> colors.gameYellow
    }
}
