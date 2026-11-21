package sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import io.github.majid460.glasskit.GlassApp
import io.github.majid460.glasskit.GlassTab

private val tabs = listOf(
    GlassTab("home", "Home", "house.fill", Icons.Filled.Home),
    GlassTab("search", "Search", "magnifyingglass", Icons.Filled.Search),
    GlassTab("profile", "Profile", "person.fill", Icons.Filled.Person),
)

@Composable
fun App() = FuturisticTheme {
    var useNav3 by rememberSaveable { mutableStateOf(true) }

    Box(
        Modifier.fillMaxSize().background(Palette.Ink).drawBehind {
            drawRect(Brush.radialGradient(listOf(Palette.Violet.copy(alpha = 0.28f), Color.Transparent), Offset(size.width * 0.95f, 0f), size.width))
            drawRect(Brush.radialGradient(listOf(Palette.Cyan.copy(alpha = 0.18f), Color.Transparent), Offset(0f, size.height * 0.55f), size.width))
        },
    ) {
        Surface(color = Color.Transparent, contentColor = Palette.Text) {
            GlassApp(
                tabs = tabs,
                useNavigation3 = useNav3,
                drawerContent = { nav -> AppDrawer(nav, useNav3) { useNav3 = it } },
                screens = {
                    screen("home") { HomeScreen(it) }
                    screen("search") { SearchScreen(it) }
                    screen("profile") { ProfileScreen(it) }
                    screen("detail") { DetailScreen(it) }
                    screen("settings") { SettingsScreen(it, useNav3) { v -> useNav3 = v } }
                },
            )
        }
    }
}
