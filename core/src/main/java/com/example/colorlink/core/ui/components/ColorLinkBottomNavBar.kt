package com.example.colorlink.core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.colorlink.core.ui.theme.ColorLinkTheme

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun ColorLinkBottomNavBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = ColorLinkTheme.colors.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = modifier.height(80.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = ColorLinkTheme.typography.labelSm
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ColorLinkTheme.colors.onPrimary,
                    selectedTextColor = ColorLinkTheme.colors.primary,
                    indicatorColor = ColorLinkTheme.colors.primary,
                    unselectedIconColor = ColorLinkTheme.colors.onSurfaceVariant,
                    unselectedTextColor = ColorLinkTheme.colors.onSurfaceVariant
                )
            )
        }
    }
}
