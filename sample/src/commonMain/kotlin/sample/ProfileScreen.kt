package sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.majid460.glasskit.GlassScreenContext
import io.github.majid460.glasskit.glass

private val myList = movies.filter { it.rating >= 8.2 }
private val collections = listOf(
    "Neon nights" to 0,
    "Deep space" to 7,
    "Mind benders" to 10,
    "Cozy animation" to 6,
)

@Composable
fun ProfileScreen(ctx: GlassScreenContext) {
    val nav = ctx.navigator
    val listState = rememberLazyListState()
    val progress = listState.scrollProgress(range = 200f)

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(
                top = statusTop() + BarHeight,
                bottom = ctx.contentPadding.calculateBottomPadding() + 24.dp,
            ),
        ) {
            item {
                Column(
                    Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier.size(104.dp).clip(CircleShape)
                            .border(2.dp, Brush.sweepGradient(listOf(Palette.Cyan, Palette.Violet, Palette.Pink, Palette.Cyan)), CircleShape),
                    ) { GenerativeArt(3, Modifier.fillMaxSize()) }
                    Text("Majid", Modifier.padding(top = 14.dp), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text("@majid460  ·  Dublin", style = MaterialTheme.typography.bodyMedium, color = Palette.Muted)
                }
            }
            item {
                Row(Modifier.padding(horizontal = 20.dp, vertical = 22.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("128", "Watched", Modifier.weight(1f))
                    StatCard("412h", "Screen time", Modifier.weight(1f))
                    StatCard("14d", "Streak", Modifier.weight(1f))
                }
            }
            item { SectionTitle("Collections") }
            item {
                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(collections.size) { i ->
                        val (name, posterId) = collections[i]
                        val shape = RoundedCornerShape(24.dp)
                        Box(Modifier.width(220.dp).height(130.dp).clip(shape).border(1.dp, Color.White.copy(alpha = 0.14f), shape)) {
                            PosterImage(posterId, Modifier.fillMaxSize())
                            Box(Modifier.fillMaxSize().glassScrim())
                            Text(name, Modifier.align(Alignment.BottomStart).padding(16.dp), fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            item { SectionTitle("My list") }
            item { PosterRow(myList, onOpen = { nav.push("detail", it.id.toString()) }) }
            item { SectionTitle("Account") }
            item { SettingRow(Icons.Filled.Settings, "Settings & navigation engine") { nav.push("settings") } }
            item { SettingRow(Icons.Filled.Menu, "Open side menu") { nav.openDrawer() } }
        }

        ScrollScrim(progress) {
            Row(
                Modifier.fillMaxWidth().height(BarHeight).padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassIconButton(Icons.Filled.Menu, onClick = { nav.openDrawer() })
                Text(
                    "Profile",
                    Modifier.padding(start = 14.dp).alpha(progress),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private fun Modifier.glassScrim(): Modifier = this.then(
    Modifier.background(Brush.verticalGradient(0.4f to Color.Transparent, 1f to Palette.Ink.copy(alpha = 0.85f))),
)

@Composable
private fun SettingRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        Modifier.padding(horizontal = 20.dp, vertical = 5.dp).fillMaxWidth()
            .glass(RoundedCornerShape(20.dp), fillAlpha = 0.08f).clickable(onClick = onClick).padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = Palette.Cyan)
        Text(label, Modifier.weight(1f).padding(start = 14.dp), fontWeight = FontWeight.SemiBold)
        Text("›", color = Palette.Muted)
    }
}
