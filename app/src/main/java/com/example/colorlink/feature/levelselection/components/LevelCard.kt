package com.example.colorlink.feature.levelselection.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.colorlink.core.ui.components.GlassCard
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp
import com.example.colorlink.core.ui.theme.ssp
import com.example.colorlink.domain.model.LevelWithProgress

@Composable
fun LevelCard(
    levelWithProgress: LevelWithProgress,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUnlocked = levelWithProgress.isUnlocked
    val progress = levelWithProgress.progress

    val backgroundBrush = if (isUnlocked) {
        Brush.verticalGradient(
            colors = listOf(
                ColorLinkTheme.colors.gameBlue,
                ColorLinkTheme.colors.gamePurple
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.05f),
                Color.White.copy(alpha = 0.02f)
            )
        )
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(ColorLinkTheme.shapes.default)
            .then(
                if (isUnlocked && progress == null) {
                    Modifier.border(
                        2.sdp(),
                        Color.White.copy(alpha = 0.4f),
                        ColorLinkTheme.shapes.default
                    )
                } else Modifier
            )
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        // Overlay for clicked state and basic glass effect
        GlassCard(
            modifier = Modifier.fillMaxSize(),
            onClick = if (isUnlocked) {
                { onClick(levelWithProgress.level.id) }
            } else null
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isUnlocked) {
                    Text(
                        text = levelWithProgress.level.number.toString(),
                        style = ColorLinkTheme.typography.displayLg.copy(fontSize = 36.ssp()),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.sdp()))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.sdp()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
                            val isFilled = progress != null && index < progress.stars
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (isFilled) ColorLinkTheme.colors.gameYellow else Color.White.copy(
                                    alpha = 0.3f
                                ),
                                modifier = Modifier.size(18.sdp())
                            )
                        }
                    }
                } else {
                    Text(
                        text = "lock",
                        style = ColorLinkTheme.typography.labelLg,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                    Text(
                        text = levelWithProgress.level.number.toString(),
                        style = ColorLinkTheme.typography.labelSm,
                        color = Color.White.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}
