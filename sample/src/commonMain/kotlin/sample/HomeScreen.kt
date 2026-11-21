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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.majid460.glasskit.GlassNavigator
import io.github.majid460.glasskit.GlassScreenContext
import kotlin.random.Random

// Built once, not on every recomposition.
private val heroMovies = movies.take(5)
private val trending = movies.sortedByDescending { it.rating }.take(8)
private val top10 = movies.shuffled(Random(7)).take(10)
private val newIn2026 = movies.filter { it.year == 2026 }
private val animationAndDocs = movies.filter { it.genre == "Animation" || it.genre == "Documentary" }

@Composable
fun HomeScreen(ctx: GlassScreenContext) {
    val nav = ctx.navigator
    val open: (Movie) -> Unit = { nav.push("detail", it.id.toString()) }
    val listState = rememberLazyListState()
    val progress = listState.scrollProgress()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(
                top = statusTop() + BarHeight,
                bottom = ctx.contentPadding.calculateBottomPadding() + 24.dp,
            ),
        ) {
            item { Hero(heroMovies, open) }
            item { SectionTitle("Trending now") }
            item { PosterRow(trending, open) }
            item { SectionTitle("Top 10 today") }
            item { PosterRow(top10, open, ranked = true) }
            item { SectionTitle("New in 2026") }
            item { PosterRow(newIn2026, open) }
            item { SectionTitle("Animation & documentaries") }
            item { PosterRow(animationAndDocs, open) }
        }
        // Pinned over the list: transparent at rest, dark scrim once content scrolls under it.
        ScrollScrim(progress) { HomeBar(nav) }
    }
}

@Composable
private fun HomeBar(nav: GlassNavigator) {
    Row(
        Modifier.fillMaxWidth().height(BarHeight).padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlassIconButton(Icons.Filled.Menu, onClick = { nav.openDrawer() })
        Column(Modifier.weight(1f).padding(start = 14.dp)) {
            Text("Good evening", style = MaterialTheme.typography.labelMedium, color = Palette.Muted)
            Text("Majid", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Box(Modifier.size(46.dp).clip(CircleShape).border(1.5.dp, Palette.Cyan.copy(alpha = 0.7f), CircleShape)) {
            GenerativeArt(3, Modifier.fillMaxSize())
        }
    }
}

@Composable
internal fun PosterRow(items: List<Movie>, onOpen: (Movie) -> Unit, ranked: Boolean = false) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        itemsIndexed(items, key = { _, m -> m.id }) { index, m ->
            PosterCard(m, onClick = { onOpen(m) }, modifier = Modifier.width(150.dp), rank = if (ranked) index + 1 else null)
        }
    }
}

@Composable
private fun Hero(items: List<Movie>, onOpen: (Movie) -> Unit) {
    val pager = rememberPagerState { items.size }
    HorizontalPager(
        state = pager,
        contentPadding = PaddingValues(horizontal = 20.dp),
        pageSpacing = 14.dp,
    ) { page ->
        val m = items[page]
        val shape = RoundedCornerShape(30.dp)
        Box(
            Modifier.fillMaxWidth().height(420.dp).clip(shape)
                .border(1.dp, Color.White.copy(alpha = 0.16f), shape)
                .clickable { onOpen(m) },
        ) {
            PosterImage(m.id, Modifier.fillMaxSize())
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(0.45f to Color.Transparent, 1f to Palette.Ink.copy(alpha = 0.95f)),
                ),
            )
            Column(Modifier.align(Alignment.BottomStart).padding(22.dp)) {
                Text(m.genre.uppercase(), style = MaterialTheme.typography.labelMedium, color = Palette.Cyan, fontWeight = FontWeight.Bold)
                Text(m.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = Color.White)
                Text(m.tagline, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFC9D2F0))
                Row(Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    GradientButton("Watch", onClick = { onOpen(m) }, icon = Icons.Filled.PlayArrow)
                    Box(Modifier.width(10.dp))
                    GlassIconButton(Icons.Filled.Add, onClick = { })
                }
            }
        }
    }
    Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.Center) {
        repeat(items.size) { i ->
            val on = pager.currentPage == i
            Box(
                Modifier.padding(horizontal = 3.dp).height(6.dp).width(if (on) 22.dp else 6.dp)
                    .clip(CircleShape).background(if (on) Palette.Cyan else Color.White.copy(alpha = 0.25f)),
            )
        }
    }
}
