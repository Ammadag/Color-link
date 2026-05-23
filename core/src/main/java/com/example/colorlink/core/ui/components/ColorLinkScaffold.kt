package com.example.colorlink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.colorlink.core.ui.theme.ColorLinkTheme

@Composable
fun ColorLinkScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (modifier: Modifier) -> Unit
) {
    val backgroundColor = ColorLinkTheme.colors.background
    
    // As per design-system.md: Radial glow: rgba(77, 142, 255, 0.10), top center
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4D8EFF).copy(alpha = 0.10f),
            backgroundColor
        ),
        startY = 0f,
        endY = 1000f // Approximate height for the glow
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = bottomBar,
        containerColor = backgroundColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
        ) {
            content(Modifier)
        }
    }
}
