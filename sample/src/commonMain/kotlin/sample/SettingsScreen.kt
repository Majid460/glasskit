package sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.majid460.glasskit.GlassBackButton
import io.github.majid460.glasskit.GlassScreenContext
import io.github.majid460.glasskit.glass

@Composable
fun SettingsScreen(ctx: GlassScreenContext, useNav3: Boolean, onUseNav3Change: (Boolean) -> Unit) {
    val nav = ctx.navigator
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp, end = 20.dp,
                top = statusTop() + BarHeight + 8.dp,
                bottom = ctx.contentPadding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Column(Modifier.fillMaxWidth().glass(RoundedCornerShape(24.dp), fillAlpha = 0.09f).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Use Navigation 3", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (useNav3) "Screens shown by Navigation 3 (NavDisplay)" else "Screens shown by GlassKit's built-in host",
                                style = MaterialTheme.typography.bodySmall, color = Palette.Muted,
                            )
                        }
                        Switch(checked = useNav3, onCheckedChange = onUseNav3Change)
                    }
                    Text(
                        "GlassApp(useNavigation3 = $useNav3). Back stacks live in the library's navigator, so flipping this keeps your place.",
                        Modifier.padding(top = 12.dp), style = MaterialTheme.typography.bodySmall, color = Palette.Muted,
                    )
                }
            }
            item {
                Column(Modifier.fillMaxWidth().glass(RoundedCornerShape(24.dp), fillAlpha = 0.09f).padding(20.dp)) {
                    Text("Live navigation state", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Tab: ${nav.selectedTabId}", Modifier.padding(top = 8.dp), color = Palette.Cyan)
                    Text("Route: ${nav.currentRoute}", color = Palette.Cyan)
                    Text("Stack: ${nav.backStackSummary}", color = Palette.Cyan)
                }
            }
            item {
                GradientButton("Open side menu", onClick = { nav.openDrawer() }, icon = Icons.Filled.Menu)
            }
        }

        // Always-pinned bar: content scrolls under it.
        ScrollScrim(progress = 1f) {
            Row(
                Modifier.fillMaxWidth().height(BarHeight).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassBackButton(onClick = { nav.pop() })
                Text("Settings", Modifier.padding(start = 14.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}
