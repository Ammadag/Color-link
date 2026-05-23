package com.example.colorlink.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp
import com.example.colorlink.core.ui.theme.ssp

@Composable
fun PrimaryGlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "button-scale"
    )

    val primaryContainer = ColorLinkTheme.colors.primaryContainer
    val primaryFixedDim = ColorLinkTheme.colors.primaryFixedDim

    val brush = Brush.verticalGradient(
        colors = listOf(primaryContainer, primaryFixedDim)
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 16.sdp(),
                shape = ColorLinkTheme.shapes.full,
                spotColor = Color(0xFF4D8EFF).copy(alpha = 0.35f),
                ambientColor = Color(0xFF4D8EFF).copy(alpha = 0.35f)
            )
            .height(50.sdp()) // Taller as per design image
            .clip(ColorLinkTheme.shapes.full)
            .background(brush)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ColorLinkTheme.colors.onPrimary,
                    modifier = Modifier.size(24.sdp())
                )
                Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.stackMd))
            }
            Text(
                text = text,
                style = ColorLinkTheme.typography.displayLg.copy(
                    fontSize = 18.ssp(),
                    letterSpacing = 2.ssp()
                ),
                color = ColorLinkTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
