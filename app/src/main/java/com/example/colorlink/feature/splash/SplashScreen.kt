package com.example.colorlink.feature.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorlink.R
import com.example.colorlink.core.ui.theme.ColorLinkTheme

/**
 * Premium Splash Screen for Color Link.
 * Implementation strictly follows:
 * - AGENTS.md (Architecture & MVI)
 * - docs/design-system.md (Colors, Spacing, Typography, Glows)
 * - docs/compose-ui.md (Stateless UI)
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    val colors = ColorLinkTheme.colors
    val spacing = ColorLinkTheme.spacing
    val typography = ColorLinkTheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // 1. App Background Radial Glow (docs/design-system.md)
        // rgba(77, 142, 255, 0.10), top center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colors.primaryContainer.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = Offset(0.5f, 0f),
                        radius = 1000f
                    )
                )
        )

        // 2. Premium Animated Background Orbs
        AnimatedBackgroundOrbs(colors.tertiary, colors.secondary)

        // 3. Main Content
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(spacing.containerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 4. Logo Icon with Glass Treatment (5% white alpha, 10% border)
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.10f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.flow_logo),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(spacing.stackLg))

            // 5. App Title (displayLg token)
            Text(
                text = "Color Link",
                style = typography.displayLg,
                color = colors.onSurface
            )

            Spacer(modifier = Modifier.height(spacing.stackSm))

            // 6. Tagline (bodyLg token)
            Text(
                text = "Connect the colors. Fill the board.",
                style = typography.bodyLg,
                color = colors.onSurfaceVariant
            )
        }

        // 7. Pulsing Indicator Dots (Bottom Center)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.stackMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dot pulse | 1800–2200ms (docs/design-system.md)
            val duration = 2000
            PulsingDot(color = colors.tertiary, delay = 0, duration = duration)
            PulsingDot(color = colors.secondary, delay = 300, duration = duration)
            PulsingDot(color = colors.onSurface, delay = 600, duration = duration)
        }
    }
}

@Composable
private fun PulsingDot(color: Color, delay: Int, duration: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dotPulsing")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration / 2, delayMillis = delay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration / 2, delayMillis = delay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(scale)
            .background(color.copy(alpha = alpha), CircleShape)
    )
}

@Composable
private fun AnimatedBackgroundOrbs(cyan: Color, lavender: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    val movement by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbMovement"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset(x = (-50).dp + movement.dp, y = (-50).dp)
                .size(350.dp)
                .blur(100.dp)
                .background(cyan.copy(alpha = 0.10f), CircleShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp - movement.dp, y = 50.dp)
                .size(450.dp)
                .blur(100.dp)
                .background(lavender.copy(alpha = 0.10f), CircleShape)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    ColorLinkTheme {
        SplashScreen()
    }
}
