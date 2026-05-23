package com.example.colorlink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.theme.ColorLinkTheme

@Composable
fun ColorLinkTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    Column(
        modifier = modifier
            .padding(top=22.dp)
            .fillMaxWidth()
            .background(ColorLinkTheme.colors.surface.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = ColorLinkTheme.spacing.gutter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationIcon != null) {
                navigationIcon()
                Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.stackMd))
            }

            Box(modifier = Modifier.weight(1f)) {
                title()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
        // Bottom border: white 10% opacity as per design-system.md
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.10f))
        )
    }
}
