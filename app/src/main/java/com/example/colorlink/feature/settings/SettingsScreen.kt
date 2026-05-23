package com.example.colorlink.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.components.GlassCard
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.ui.components.ColorLinkMainBottomNavBar
import com.example.colorlink.ui.components.ColorLinkMainTopBar
import com.example.colorlink.ui.navigation.Screen

@Composable
fun SettingsScreen(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    ColorLinkScaffold(
        modifier = modifier,
        topBar = {
            ColorLinkMainTopBar(
                title = "Settings",
                coins = 1250, // These should ideally come from state, but using placeholders for now
                hints = 10,
                showLogo = false
            )
        },
        bottomBar = {
            ColorLinkMainBottomNavBar(
                currentRoute = Screen.Settings.route,
                onNavigate = { route ->
                    when (route) {
                        Screen.Home.route -> onIntent(SettingsIntent.BackClicked) // Or specific intent to home
                        Screen.LevelSelection.route -> { /* Navigate to levels */ }
                        Screen.DailyPuzzle.route -> { /* Navigate to daily */ }
                    }
                }
            )
        }
    ) { innerModifier ->
        Column(
            modifier = innerModifier
                .fillMaxSize()
                .padding(ColorLinkTheme.spacing.containerPadding),
            verticalArrangement = Arrangement.spacedBy(ColorLinkTheme.spacing.stackMd)
        ) {
            SettingsToggleRow(
                label = "Sound Effects",
                enabled = state.settings.soundEnabled,
                onToggle = { onIntent(SettingsIntent.ToggleSound(it)) }
            )

            SettingsToggleRow(
                label = "Music",
                enabled = state.settings.musicEnabled,
                onToggle = { onIntent(SettingsIntent.ToggleMusic(it)) }
            )

            SettingsToggleRow(
                label = "Haptic Feedback",
                enabled = state.settings.hapticsEnabled,
                onToggle = { onIntent(SettingsIntent.ToggleHaptics(it)) }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Version 1.0.0",
                style = ColorLinkTheme.typography.labelSm,
                color = ColorLinkTheme.colors.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(80.dp)) // Extra space for bottom bar
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = ColorLinkTheme.typography.bodyLg,
                color = ColorLinkTheme.colors.onSurface
            )
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ColorLinkTheme.colors.primary,
                    checkedTrackColor = ColorLinkTheme.colors.primaryContainer,
                    uncheckedThumbColor = ColorLinkTheme.colors.outline,
                    uncheckedTrackColor = ColorLinkTheme.colors.surfaceContainerHighest
                )
            )
        }
    }
}
