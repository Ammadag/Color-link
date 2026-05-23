package com.example.colorlink.feature.levelselection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.feature.levelselection.components.LevelCard
import com.example.colorlink.ui.components.ColorLinkMainBottomNavBar
import com.example.colorlink.ui.components.ColorLinkMainTopBar
import com.example.colorlink.ui.navigation.Screen

@Composable
fun LevelSelectionScreen(
    state: LevelSelectionState,
    onIntent: (LevelSelectionIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    ColorLinkScaffold(
        modifier = modifier,
        topBar = {
            ColorLinkMainTopBar(
                title = "Levels",
                coins = state.coins,
                hints = state.hints,
                showLogo = false
            )
        },
        bottomBar = {
            ColorLinkMainBottomNavBar(
                currentRoute = Screen.LevelSelection.route,
                onNavigate = { route ->
                    when (route) {
                        Screen.Home.route -> onIntent(LevelSelectionIntent.TabClicked(0))
                        Screen.DailyPuzzle.route -> onIntent(LevelSelectionIntent.TabClicked(2))
                        Screen.Settings.route -> onIntent(LevelSelectionIntent.TabClicked(3))
                    }
                }
            )
        }
    ) { innerModifier ->
        Box(modifier = innerModifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = ColorLinkTheme.colors.primary
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    contentPadding = PaddingValues(
                        start = ColorLinkTheme.spacing.containerPadding,
                        end = ColorLinkTheme.spacing.containerPadding,
                        top = ColorLinkTheme.spacing.containerPadding,
                        bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd),
                    verticalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.levels) { levelWithProgress ->
                        LevelCard(
                            levelWithProgress = levelWithProgress,
                            onClick = { onIntent(LevelSelectionIntent.LevelClicked(it)) }
                        )
                    }
                }
            }
        }
    }
}
