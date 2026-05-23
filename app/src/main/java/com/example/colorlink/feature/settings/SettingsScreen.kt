package com.example.colorlink.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.colorlink.core.ui.components.ColorLinkScaffold
import com.example.colorlink.core.ui.components.GlassCard
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp
import com.example.colorlink.core.ui.theme.ssp
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
                coins = 1250,
                hints = 10,
                showLogo = false,
                navigationIcon = {
                    IconButton(onClick = { onIntent(SettingsIntent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ColorLinkTheme.colors.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            ColorLinkMainBottomNavBar(
                currentRoute = Screen.Settings.route,
                onNavigate = { route -> onIntent(SettingsIntent.TabClicked(route)) }
            )
        }
    ) { innerModifier ->
        Column(
            modifier = innerModifier
                .fillMaxSize()
                .padding(horizontal = ColorLinkTheme.spacing.containerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackMd))

            Text(
                text = "Settings",
                style = ColorLinkTheme.typography.displayLg.copy(fontSize = 27.ssp()),
                color = ColorLinkTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Customize your puzzle experience.",
                style = ColorLinkTheme.typography.bodyMd,
                color = ColorLinkTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackLg))

            // FEEDBACK SECTION
            SettingsSection(title = "FEEDBACK") {
                SettingsToggleItem(
                    icon = Icons.Default.VolumeUp,
                    label = "Sound Effects",
                    enabled = state.settings.soundEnabled,
                    onToggle = { onIntent(SettingsIntent.ToggleSound(it)) }
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 1.sdp())
                SettingsToggleItem(
                    icon = Icons.Default.MusicNote,
                    label = "Background Music",
                    enabled = state.settings.musicEnabled,
                    onToggle = { onIntent(SettingsIntent.ToggleMusic(it)) }
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 1.sdp())
                SettingsToggleItem(
                    icon = Icons.Default.Vibration,
                    label = "Haptic Feedback",
                    enabled = state.settings.hapticsEnabled,
                    onToggle = { onIntent(SettingsIntent.ToggleHaptics(it)) }
                )
            }

            Spacer(modifier = Modifier.height(ColorLinkTheme.spacing.stackMd))

            // GENERAL SECTION
            SettingsSection(title = "GENERAL") {
                SettingsToggleItem(
                    icon = Icons.Default.NotificationsNone,
                    label = "Notifications",
                    enabled = state.settings.notificationsEnabled,
                    onToggle = { onIntent(SettingsIntent.ToggleNotifications(it)) }
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 1.sdp())
                SettingsClickItem(
                    icon = Icons.Default.Public,
                    label = "Language",
                    value = state.settings.language,
                    onClick = { /* Show language selector */ }
                )
            }

            Spacer(modifier = Modifier.height(12.sdp()))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Privacy Policy",
                    style = ColorLinkTheme.typography.labelSm,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { }
                )
                Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.stackMd))
                Text(
                    text = "Terms of Service",
                    style = ColorLinkTheme.typography.labelSm,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(7.sdp()))

            Text(
                text = "VERSION 1.0.4",
                style = ColorLinkTheme.typography.labelSm,
                color = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(83.sdp())) // Space for bottom bar
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = ColorLinkTheme.typography.labelLg,
            color = ColorLinkTheme.colors.gameCyan,
            letterSpacing = 1.ssp(),
            modifier = Modifier.padding(start = 10.sdp(), bottom = 7.sdp())
        )
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = ColorLinkTheme.shapes.large
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.sdp()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(17.sdp())
            )
            Spacer(modifier = Modifier.width(13.sdp()))
            Text(
                text = label,
                style = ColorLinkTheme.typography.bodyLg,
                color = Color.White
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ColorLinkTheme.colors.primary,
                uncheckedThumbColor = Color.White.copy(alpha = 0.4f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun SettingsClickItem(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 13.sdp()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(17.sdp())
            )
            Spacer(modifier = Modifier.width(13.sdp()))
            Text(
                text = label,
                style = ColorLinkTheme.typography.bodyLg,
                color = Color.White
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = ColorLinkTheme.typography.bodyMd,
                color = Color.White.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.width(7.sdp()))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(13.sdp())
            )
        }
    }
}
