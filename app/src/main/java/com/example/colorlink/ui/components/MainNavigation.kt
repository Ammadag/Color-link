package com.example.colorlink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.BottomNavItem
import com.example.colorlink.core.ui.components.ColorLinkBottomNavBar
import com.example.colorlink.core.ui.components.ColorLinkStatBadge
import com.example.colorlink.core.ui.components.ColorLinkTopAppBar
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.ui.navigation.Screen

@Composable
fun ColorLinkMainTopBar(
    coins: Int,
    hints: Int,
    modifier: Modifier = Modifier,
    title: String = "Color Link",
    showLogo: Boolean = true
) {
    ColorLinkTopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showLogo) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        ColorLinkTheme.colors.gameCyan,
                                        ColorLinkTheme.colors.gamePurple
                                    )
                                )
                            )
                    )
                    Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.unit * 2))
                }
                Text(
                    text = title,
                    style = ColorLinkTheme.typography.headlineMd.copy(fontWeight = FontWeight.Bold),
                    color = ColorLinkTheme.colors.onSurface
                )
            }
        },
        actions = {
            ColorLinkStatBadge(
                icon = Icons.Default.MonetizationOn,
                value = "$coins",
                containerColor = ColorLinkTheme.colors.secondary.copy(alpha = 0.2f),
                contentColor = ColorLinkTheme.colors.secondary
            )
            Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.stackSm))
            ColorLinkStatBadge(
                icon = Icons.Default.Lightbulb,
                value = "$hints",
                containerColor = ColorLinkTheme.colors.tertiary.copy(alpha = 0.2f),
                contentColor = ColorLinkTheme.colors.tertiary
            )
        }
    )
}

@Composable
fun ColorLinkMainBottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.Home.route),
        BottomNavItem("Levels", Icons.Default.Apps, Screen.LevelSelection.route),
        BottomNavItem("Settings", Icons.Default.Settings, Screen.Settings.route)
    )

    ColorLinkBottomNavBar(
        items = items,
        currentRoute = currentRoute,
        onItemClick = { item -> 
            if (currentRoute != item.route) {
                onNavigate(item.route)
            }
        },
        modifier = modifier
    )
}
