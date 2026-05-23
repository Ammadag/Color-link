package com.example.colorlink.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.theme.ColorLinkTheme

@Composable
fun ControlCircleButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int? = null,
    isPrimary: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        label = "control-button-scale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BadgedBox(
            badge = {
                if (badgeCount != null) {
                    Badge(
                        containerColor = ColorLinkTheme.colors.secondaryContainer,
                        contentColor = ColorLinkTheme.colors.onSecondaryContainer
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            style = ColorLinkTheme.typography.labelSm,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(CircleShape)
                    .background(
                        if (isPrimary) ColorLinkTheme.colors.primaryContainer 
                        else Color.White.copy(alpha = 0.05f)
                    )
                    .then(
                        if (!isPrimary) Modifier.border(1.dp, Color.White.copy(alpha = 0.10f), CircleShape)
                        else Modifier
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isPrimary) ColorLinkTheme.colors.onPrimaryContainer else ColorLinkTheme.colors.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.unit))
        
        Text(
            text = label,
            style = ColorLinkTheme.typography.labelSm,
            color = ColorLinkTheme.colors.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}
