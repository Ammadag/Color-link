package com.example.colorlink.feature.gameplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.components.ColorLinkTopAppBar
import com.example.colorlink.core.ui.components.ControlCircleButton
import com.example.colorlink.core.ui.components.GlassCard
import com.example.colorlink.core.ui.components.PrimaryGlowButton
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp
import com.example.colorlink.core.ui.theme.ssp
import com.example.colorlink.domain.model.BoardPosition
import com.example.colorlink.domain.model.Dot
import com.example.colorlink.domain.model.DotColor
import com.example.colorlink.domain.model.Level
import com.example.colorlink.feature.gameplay.components.GameBoard

@Composable
fun GameplayScreen(
    state: GameplayState,
    onIntent: (GameplayIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        ColorLinkScaffold(
            topBar = {
                ColorLinkTopAppBar(
                    title = {
                        Column {
                            Text(
                                text = state.level?.let { "Level ${it.number}" } ?: "Loading...",
                                style = ColorLinkTheme.typography.headlineMd,
                                color = ColorLinkTheme.colors.onSurface
                            )
                            Text(
                                text = "Moves: ${state.moveCount}",
                                style = ColorLinkTheme.typography.labelSm,
                                color = ColorLinkTheme.colors.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { onIntent(GameplayIntent.BackClicked) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = ColorLinkTheme.colors.onSurface
                            )
                        }
                    }
                )
            }
        ) { innerModifier ->
            Column(
                modifier = innerModifier
                    .fillMaxSize()
                    .padding(ColorLinkTheme.spacing.containerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.level != null) {
                        GameBoard(
                            level = state.level,
                            paths = state.paths,
                            activePath = state.activePath,
                            hintPath = state.hintPath,
                            onStartDrag = { onIntent(GameplayIntent.StartPath(it)) },
                            onDragTo = { onIntent(GameplayIntent.DragTo(it)) },
                            onEndDrag = { onIntent(GameplayIntent.EndPath) }
                        )
                    } else if (state.isLoading) {
                        CircularProgressIndicator(color = ColorLinkTheme.colors.primary)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = ColorLinkTheme.spacing.stackLg),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    ControlCircleButton(
                        icon = Icons.Default.SettingsBackupRestore,
                        label = "Undo",
                        onClick = { onIntent(GameplayIntent.Undo) }
                    )

                    ControlCircleButton(
                        icon = Icons.Default.Lightbulb,
                        label = "Hint",
                        onClick = { onIntent(GameplayIntent.UseHint) },
                        badgeCount = state.hintCount,
                        isPrimary = true
                    )

                    ControlCircleButton(
                        icon = Icons.Default.Refresh,
                        label = "Restart",
                        onClick = { onIntent(GameplayIntent.Restart) }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = state.isCompleted,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LevelCompleteOverlay(
                stars = state.stars,
                time = formatTime(state.timeSeconds),
                moves = state.moveCount,
                best = state.bestMoves ?: state.moveCount,
                onNextLevel = { onIntent(GameplayIntent.LevelCompleteNextLevelClicked) },
                onReplay = { onIntent(GameplayIntent.LevelCompleteReplayClicked) },
                onLevels = { onIntent(GameplayIntent.LevelCompleteLevelsClicked) }
            )
        }
    }
}

@Composable
private fun LevelCompleteOverlay(
    stars: Int,
    time: String,
    moves: Int,
    best: Int,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onLevels: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(ColorLinkTheme.spacing.containerPadding),
            shape = ColorLinkTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ColorLinkTheme.spacing.stackLg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Level\nComplete!",
                    style = ColorLinkTheme.typography.displayLg.copy(fontSize = 27.ssp()),
                    color = ColorLinkTheme.colors.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 37.ssp()
                )

                Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(7.sdp())
                ) {
                    StarIcon(filled = stars >= 1, size = 40.sdp())
                    StarIcon(filled = stars >= 3, size = 53.sdp())
                    StarIcon(filled = stars >= 2, size = 40.sdp())
                }

                Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ColorLinkTheme.shapes.large)
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(ColorLinkTheme.spacing.stackMd)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(label = "TIME", value = time)
                        Box(
                            modifier = Modifier
                                .width(1.sdp())
                                .height(20.sdp())
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        StatItem(label = "MOVES", value = moves.toString())
                        Box(
                            modifier = Modifier
                                .width(1.sdp())
                                .height(20.sdp())
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        StatItem(label = "BEST", value = best.toString(), isHighlight = true)
                    }
                }

                Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

                PrimaryGlowButton(
                    text = "Next Level",
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = onNextLevel,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackMd))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd)
                ) {
                    SecondaryActionButton(
                        text = "Replay",
                        icon = Icons.Default.Refresh,
                        onClick = onReplay,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryActionButton(
                        text = "Levels",
                        icon = Icons.Default.Apps,
                        onClick = onLevels,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StarIcon(filled: Boolean, size: Dp) {
    Icon(
        imageVector = if (filled) Icons.Default.Star else Icons.Outlined.Star,
        contentDescription = null,
        tint = if (filled) ColorLinkTheme.colors.gameCyan else Color.White.copy(alpha = 0.2f),
        modifier = Modifier.size(size)
    )
}

@Composable
private fun StatItem(label: String, value: String, isHighlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = ColorLinkTheme.typography.labelSm,
            color = ColorLinkTheme.colors.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = ColorLinkTheme.typography.headlineMd,
            color = if (isHighlight) ColorLinkTheme.colors.gameCyan else ColorLinkTheme.colors.onSurface
        )
    }
}

@Composable
private fun SecondaryActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(ColorLinkTheme.shapes.full)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.05f))
                ),
                shape = ColorLinkTheme.shapes.full
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = ColorLinkTheme.typography.labelLg,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun GameplayScreenPreview() {
    ColorLinkTheme {
        GameplayScreen(
            state = GameplayState(
                level = Level(
                    id = "level_1",
                    number = 1,
                    rows = 5,
                    columns = 5,
                    dots = listOf(
                        Dot("1", DotColor.Blue, BoardPosition(0, 0)),
                        Dot("2", DotColor.Blue, BoardPosition(4, 4))
                    )
                ),
                moveCount = 12,
                timeSeconds = 45,
                hintCount = 2
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun LevelCompleteOverlayPreview() {
    ColorLinkTheme {
        LevelCompleteOverlay(
            stars = 3,
            time = "0:45",
            moves = 12,
            best = 10,
            onNextLevel = {},
            onReplay = {},
            onLevels = {}
        )
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}
