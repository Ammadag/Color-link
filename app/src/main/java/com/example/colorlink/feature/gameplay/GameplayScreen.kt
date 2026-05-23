package com.example.colorlink.feature.gameplay

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.*
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.feature.gameplay.components.GameBoard

@Composable
fun GameplayScreen(
    state: GameplayState,
    onIntent: (GameplayIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    ColorLinkScaffold(
        modifier = modifier,
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

    if (state.isCompleted) {
        LevelCompleteDialog(
            onContinue = { onIntent(GameplayIntent.LevelCompleteContinueClicked) }
        )
    }
}

@Composable
private fun LevelCompleteDialog(
    onContinue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, 
        containerColor = ColorLinkTheme.colors.surfaceContainerHigh,
        title = {
            Text(
                text = "LEVEL COMPLETE!",
                style = ColorLinkTheme.typography.headlineLg,
                color = ColorLinkTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "You've filled the board and connected all colors.",
                style = ColorLinkTheme.typography.bodyMd,
                color = ColorLinkTheme.colors.onSurface
            )
        },
        confirmButton = {
            PrimaryGlowButton(
                text = "CONTINUE",
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
