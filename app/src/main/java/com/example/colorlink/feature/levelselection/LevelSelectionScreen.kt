package com.example.colorlink.feature.levelselection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.components.ColorLinkStatBadge
import com.example.colorlink.core.ui.components.ColorLinkTopAppBar
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp
import com.example.colorlink.core.ui.theme.ssp
import com.example.colorlink.feature.levelselection.components.LevelCard
import com.example.colorlink.ui.components.ColorLinkMainBottomNavBar
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
            ColorLinkTopAppBar(
                title = {
                    Text(
                        text = "Levels",
                        style = ColorLinkTheme.typography.headlineMd.copy(fontWeight = FontWeight.Bold),
                        color = ColorLinkTheme.colors.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(LevelSelectionIntent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ColorLinkTheme.colors.onSurface
                        )
                    }
                },
                actions = {
                    ColorLinkStatBadge(
                        value = state.coins.toString(),
                        icon = Icons.Default.MonetizationOn,
                        containerColor = Color.White.copy(alpha = 0.05f),
                        contentColor = ColorLinkTheme.colors.gameCyan
                    )
                }
            )
        },
        bottomBar = {
            ColorLinkMainBottomNavBar(
                currentRoute = Screen.LevelSelection.route,
                onNavigate = { route -> onIntent(LevelSelectionIntent.TabClicked(route)) }
            )
        }
    ) { innerModifier ->
        Column(
            modifier = innerModifier
                .fillMaxSize()
                .padding(horizontal = ColorLinkTheme.spacing.containerPadding)
        ) {
            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackMd))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose Level",
                    style = ColorLinkTheme.typography.headlineLg.copy(fontSize = 27.ssp()),
                    color = ColorLinkTheme.colors.onSurface
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(horizontal = 10.sdp(), vertical = 3.sdp())
                ) {
                    Text(
                        text = "${state.completedCount} / ${state.totalCount} completed",
                        style = ColorLinkTheme.typography.labelSm,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackMd))

            val packs = listOf(
                "Beginner" to "classic",
                "Easy" to "easy",
                "Medium" to "medium",
                "Hard" to "hard"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.sdp()),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(packs) { (name, id) ->
                    PackTab(
                        label = name,
                        isSelected = state.selectedPackId == id,
                        onClick = { onIntent(LevelSelectionIntent.PackSelected(id)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ColorLinkTheme.colors.primary
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(13.sdp()),
                        verticalArrangement = Arrangement.spacedBy(13.sdp()),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 83.sdp())
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
}

@Composable
private fun PackTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) ColorLinkTheme.colors.primary else Color.White.copy(alpha = 0.05f))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.sdp(), vertical = 10.sdp()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = ColorLinkTheme.typography.labelLg,
            color = if (isSelected) ColorLinkTheme.colors.onPrimary else Color.White.copy(alpha = 0.6f)
        )
    }
}
