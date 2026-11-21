package sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.majid460.glasskit.GlassScreenContext
import io.github.majid460.glasskit.glass

@Composable
fun SearchScreen(ctx: GlassScreenContext) {
    var query by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("All") }
    val results = remember(query, genre) {
        val q = query.trim()
        movies.filter { m ->
            (genre == "All" || m.genre == genre) &&
                (q.isEmpty() || m.title.contains(q, true) || m.tagline.contains(q, true) ||
                    m.genre.contains(q, true) || m.cast.any { it.contains(q, true) })
        }
    }
    val gridState = rememberLazyGridState()
    val progress = gridState.scrollProgress(range = 80f)
    val density = LocalDensity.current
    var headerHeight: Dp by remember { mutableStateOf(230.dp) }

    // New filter or query: start from the top of the results.
    LaunchedEffect(query, genre) { gridState.scrollToItem(0) }

    Box(Modifier.fillMaxSize()) {
        if (results.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(top = headerHeight), contentAlignment = Alignment.TopCenter) {
                Text(
                    "Nothing in this galaxy matches.\nTry another word or genre.",
                    color = Palette.Muted,
                    modifier = Modifier.padding(top = 40.dp),
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                contentPadding = PaddingValues(
                    start = 20.dp, end = 20.dp, top = headerHeight + 4.dp,
                    bottom = ctx.contentPadding.calculateBottomPadding() + 24.dp,
                ),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(results, key = { it.id }) { m ->
                    PosterCard(m, onClick = { ctx.navigator.push("detail", m.id.toString()) })
                }
            }
        }

        // Pinned header: results scroll underneath it.
        ScrollScrim(
            progress = 0.45f + 0.55f * progress,
            modifier = Modifier.onSizeChanged { headerHeight = with(density) { it.height.toDp() } },
        ) {
            Column(Modifier.padding(bottom = 6.dp)) {
                Text(
                    "Search",
                    Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                )
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Palette.Text, fontSize = 16.sp),
                    cursorBrush = SolidColor(Palette.Cyan),
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    decorationBox = { inner ->
                        Row(
                            Modifier.fillMaxWidth().glass(CircleShape, fillAlpha = 0.10f)
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Filled.Search, null, tint = Palette.Cyan)
                            Box(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                                if (query.isEmpty()) Text("Titles, genres, actors…", color = Palette.Muted)
                                inner()
                            }
                            if (query.isNotEmpty()) {
                                Icon(Icons.Filled.Close, "Clear", Modifier.size(20.dp).clickable { query = "" }, tint = Palette.Muted)
                            }
                        }
                    },
                )
                LazyRow(
                    Modifier.padding(top = 14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(genres) { g -> FilterChip(g, selected = g == genre, onClick = { genre = g }) }
                }
                Text(
                    "${results.size} ${if (results.size == 1) "result" else "results"}",
                    Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Palette.Muted,
                )
            }
        }
    }
}
