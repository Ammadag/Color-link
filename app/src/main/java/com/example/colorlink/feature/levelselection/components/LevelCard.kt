package com.example.colorlink.feature.levelselection.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.GlassCard
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.domain.model.LevelWithProgress

@Composable
fun LevelCard(
    levelWithProgress: LevelWithProgress,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUnlocked = levelWithProgress.isUnlocked
    val progress = levelWithProgress.progress

    GlassCard(
        modifier = modifier.aspectRatio(1f),
        onClick = if (isUnlocked) { { onClick(levelWithProgress.level.id) } } else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isUnlocked) {
                Text(
                    text = levelWithProgress.level.number.toString(),
                    style = ColorLinkTheme.typography.headlineMd,
                    color = ColorLinkTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.unit))
                
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (progress != null && index < progress.stars) {
                                ColorLinkTheme.colors.gameYellow
                            } else {
                                Color.White.copy(alpha = 0.2f)
                            },
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = ColorLinkTheme.colors.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
