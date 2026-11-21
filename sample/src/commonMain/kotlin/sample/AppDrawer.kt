package sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.majid460.glasskit.GlassNavigator
import io.github.majid460.glasskit.glass

@Composable
fun ColumnScope.AppDrawer(navigator: GlassNavigator, useNav3: Boolean, onUseNav3Change: (Boolean) -> Unit) {
    Box(Modifier.fillMaxWidth().height(170.dp)) {
        GenerativeArt(5, Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Palette.Panel))))
        Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Text("GlassKit", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text("Native glass tabs · Navigation 3", style = MaterialTheme.typography.labelMedium, color = Palette.Cyan)
        }
    }
    Column(Modifier.padding(top = 12.dp)) {
        DrawerItem("Home", Icons.Filled.Home, navigator.selectedTabId == "home" && !navigator.canPop) {
            navigator.selectTab("home"); navigator.closeDrawer()
        }
        DrawerItem("Search", Icons.Filled.Search, navigator.selectedTabId == "search" && !navigator.canPop) {
            navigator.selectTab("search"); navigator.closeDrawer()
        }
        DrawerItem("Profile", Icons.Filled.Person, navigator.selectedTabId == "profile" && !navigator.canPop) {
            navigator.selectTab("profile"); navigator.closeDrawer()
        }
        DrawerItem("Settings", Icons.Filled.Settings, navigator.currentRoute == "settings") {
            if (navigator.currentRoute != "settings") navigator.push("settings")
            navigator.closeDrawer()
        }
    }
    HorizontalDivider(Modifier.padding(vertical = 14.dp, horizontal = 20.dp), color = Color.White.copy(alpha = 0.12f))
    Row(
        Modifier.padding(horizontal = 16.dp).fillMaxWidth().glass(RoundedCornerShape(20.dp), fillAlpha = 0.09f).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Use Navigation 3", fontWeight = FontWeight.Bold)
            Text(
                "GlassApp(useNavigation3 = $useNav3)",
                style = MaterialTheme.typography.labelSmall, color = Palette.Muted,
            )
        }
        Switch(checked = useNav3, onCheckedChange = onUseNav3Change)
    }
    Text(
        "The library owns tab + back-stack state. Flip the switch: your place is kept.",
        Modifier.padding(20.dp), style = MaterialTheme.typography.bodySmall, color = Palette.Muted,
    )
}

@Composable
private fun DrawerItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, fontWeight = FontWeight.SemiBold) },
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, null) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Palette.Violet.copy(alpha = 0.22f),
            unselectedContainerColor = Color.Transparent,
            selectedTextColor = Palette.Cyan,
            selectedIconColor = Palette.Cyan,
        ),
    )
}
