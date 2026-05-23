package com.example.colorlink.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.components.DotNode
import com.example.colorlink.core.ui.components.GlassCard
import com.example.colorlink.core.ui.components.PrimaryGlowButton
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.domain.model.DotColor
import com.example.colorlink.ui.components.ColorLinkMainBottomNavBar
import com.example.colorlink.ui.components.ColorLinkMainTopBar
import com.example.colorlink.ui.navigation.Screen

@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    ColorLinkScaffold(
        modifier = modifier,
        topBar = {
            ColorLinkMainTopBar(
                coins = state.coins,
                hints = state.hints
            )
        },
        bottomBar = {
            ColorLinkMainBottomNavBar(
                currentRoute = Screen.Home.route,
                onNavigate = { route ->
                    when (route) {
                        Screen.LevelSelection.route -> onIntent(HomeIntent.LevelSelectionClicked)
                        Screen.DailyPuzzle.route -> onIntent(HomeIntent.DailyPuzzleClicked)
                        Screen.Settings.route -> onIntent(HomeIntent.SettingsClicked)
                    }
                }
            )
        }
    ) { innerModifier ->
        LazyColumn(
            modifier = innerModifier
                .fillMaxSize()
                .padding(horizontal = ColorLinkTheme.spacing.containerPadding),
            verticalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd),
            contentPadding = PaddingValues(top = ColorLinkTheme.spacing.stackMd, bottom = 100.dp)
        ) {
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.1f),
                    shape = ColorLinkTheme.shapes.large
                ) {
                    MiniBoardPreview()
                }
            }

            item {
                PrimaryGlowButton(
                    text = "PLAY",
                    icon = Icons.Default.PlayArrow,
                    onClick = { onIntent(HomeIntent.PlayClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onIntent(HomeIntent.LevelSelectionClicked) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = null,
                            tint = ColorLinkTheme.colors.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.unit))
                        Text(
                            text = "Levels",
                            style = ColorLinkTheme.typography.headlineMd,
                            color = ColorLinkTheme.colors.onSurface
                        )
                        Text(
                            text = state.unlockedWorldInfo,
                            style = ColorLinkTheme.typography.labelSm,
                            color = ColorLinkTheme.colors.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd)
                ) {
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "CURRENT LEVEL",
                                    style = ColorLinkTheme.typography.labelSm.copy(fontSize = 10.sp),
                                    color = ColorLinkTheme.colors.onSurfaceVariant
                                )
                                Text(
                                    text = "${state.currentLevelNumber}",
                                    style = ColorLinkTheme.typography.headlineMd,
                                    color = ColorLinkTheme.colors.onSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = ColorLinkTheme.colors.onSurfaceVariant.copy(alpha = 0.3f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    GlassCard(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "STREAK",
                                    style = ColorLinkTheme.typography.labelSm.copy(fontSize = 10.sp),
                                    color = ColorLinkTheme.colors.onSurfaceVariant
                                )
                                Text(
                                    text = "${state.streakDays} Days",
                                    style = ColorLinkTheme.typography.headlineMd.copy(fontSize = 20.sp),
                                    color = ColorLinkTheme.colors.secondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = ColorLinkTheme.colors.secondary.copy(alpha = 0.4f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd)
                ) {
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onIntent(HomeIntent.DailyPuzzleClicked) }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = ColorLinkTheme.colors.tertiary
                            )
                            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.unit))
                            Text(
                                text = "Daily Puzzle",
                                style = ColorLinkTheme.typography.labelLg,
                                color = ColorLinkTheme.colors.onSurface
                            )
                        }
                    }

                    GlassCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onIntent(HomeIntent.SettingsClicked) }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = ColorLinkTheme.colors.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.unit))
                            Text(
                                text = "Settings",
                                style = ColorLinkTheme.typography.labelLg,
                                color = ColorLinkTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniBoardPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val rows = 5
            val columns = 5
            val cellSize = size.minDimension / 6.5f
            val startX = (size.width - (columns * cellSize)) / 2
            val startY = (size.height - (rows * cellSize)) / 2

            for (r in 0 until rows) {
                for (c in 0 until columns) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.05f),
                        radius = cellSize * 0.4f,
                        center = Offset(
                            startX + c * cellSize + cellSize / 2,
                            startY + r * cellSize + cellSize / 2
                        )
                    )
                }
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val cellSize = minOf(maxWidth, maxHeight) / 6.5f
            val boardSize = cellSize * 5
            val leftOffset = (maxWidth - boardSize) / 2
            val topOffset = (maxHeight - boardSize) / 2

            val dots = listOf(
                Triple(0, 0, DotColor.Blue),
                Triple(0, 4, DotColor.Purple),
                Triple(1, 2, DotColor.Cyan),
                Triple(3, 1, DotColor.Cyan),
                Triple(4, 0, DotColor.Purple),
                Triple(4, 4, DotColor.Blue)
            )

            dots.forEach { (r, c, color) ->
                DotNode(
                    color = color,
                    modifier = Modifier
                        .offset(
                            x = leftOffset + cellSize * c + (cellSize - 32.dp) / 2,
                            y = topOffset + cellSize * r + (cellSize - 32.dp) / 2
                        ),
                    dotSize = 32.dp
                )
            }
        }
    }
}
