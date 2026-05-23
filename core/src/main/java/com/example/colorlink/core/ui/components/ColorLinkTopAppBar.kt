package com.example.colorlink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp

@Composable
fun ColorLinkTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    Column(
        modifier = modifier
            .padding(top = 22.sdp())
            .fillMaxWidth()
            .background(ColorLinkTheme.colors.surface.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.sdp())
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
                .height(1.sdp())
                .background(Color.White.copy(alpha = 0.10f))
        )
    }
}
