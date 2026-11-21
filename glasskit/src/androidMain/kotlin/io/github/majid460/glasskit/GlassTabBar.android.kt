package io.github.majid460.glasskit

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal actual fun GlassTabBar(
    tabs: List<GlassTab>,
    selectedTabId: String,
    onTabSelected: (String) -> Unit,
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f), tonalElevation = 0.dp) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = tab.id == selectedTabId,
                onClick = { onTabSelected(tab.id) },
                icon = { Icon(tab.icon, contentDescription = tab.title) },
                label = { Text(tab.title) },
            )
        }
    }
}
