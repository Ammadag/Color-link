package com.example.colorlink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.colorlink.core.ui.theme.ColorLinkTheme
import com.example.colorlink.core.ui.theme.sdp
import com.example.colorlink.core.ui.theme.ssp

@Composable
fun ColorLinkStatBadge(
    icon: ImageVector,
    value: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(ColorLinkTheme.shapes.full)
            .background(containerColor)
            .padding(horizontal = 8.sdp(), vertical = 4.sdp()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(16.sdp())
        )
        Spacer(modifier = Modifier.width(ColorLinkTheme.spacing.unit))
        Text(
            text = value,
            style = ColorLinkTheme.typography.labelLg,
            color = ColorLinkTheme.colors.onSurface,
            fontSize = 12.ssp()
        )
    }
}
