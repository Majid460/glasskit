package sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.majid460.glasskit.GlassBackButton
import io.github.majid460.glasskit.GlassScreenContext
import io.github.majid460.glasskit.glass

@Composable
fun DetailScreen(ctx: GlassScreenContext) {
    val movie = ctx.arg?.toIntOrNull()?.let(::movieById)
    if (movie == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Movie not found") }
        return
    }
    val nav = ctx.navigator
    val similar = movies.filter { it.genre == movie.genre && it.id != movie.id }
        .ifEmpty { movies.filter { it.id != movie.id }.take(6) }
    val listState = rememberLazyListState()
    val progress = listState.scrollProgress(range = 320f)

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(bottom = ctx.contentPadding.calculateBottomPadding() + 24.dp),
        ) {
            item {
                Box(Modifier.fillMaxWidth().height(480.dp)) {
                    PosterImage(movie.id, Modifier.fillMaxSize())
                    Box(
                        Modifier.fillMaxSize().background(
                            Brush.verticalGradient(0.55f to Color.Transparent, 1f to Palette.Ink),
                        ),
                    )
                    Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                        Text(movie.title, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = Color.White)
                        Text(movie.tagline, style = MaterialTheme.typography.bodyLarge, color = Palette.Cyan)
                    }
                }
            }
            item {
                Row(
                    Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf(movie.genre, "${movie.year}", "${movie.minutes} min", "★ ${movie.rating}").forEach { label ->
                        Text(
                            label,
                            Modifier.glass(CircleShape, fillAlpha = 0.09f).padding(horizontal = 14.dp, vertical = 7.dp),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
            item {
                Row(Modifier.padding(horizontal = 20.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    GradientButton("Play", onClick = { }, icon = Icons.Filled.PlayArrow)
                    Box(Modifier.width(10.dp))
                    GlassIconButton(Icons.Filled.Add, onClick = { })
                }
            }
            item {
                Text(movie.synopsis, Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.bodyLarge, color = Palette.Text.copy(alpha = 0.85f))
            }
            item { SectionTitle("Cast") }
            item {
                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(movie.cast) { name ->
                        Text(name, Modifier.glass(CircleShape, fillAlpha = 0.07f).padding(horizontal = 16.dp, vertical = 9.dp), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            item { SectionTitle("More like this") }
            item { PosterRow(similar, onOpen = { nav.push("detail", it.id.toString()) }) }
            item {
                Text(
                    "Back stack (managed by GlassKit): ${nav.backStackSummary}",
                    Modifier.padding(20.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Palette.Muted,
                )
            }
        }

        // Back button always visible; the bar behind it (and the title) fade in as the poster scrolls away.
        ScrollScrim(progress) {
            Row(
                Modifier.fillMaxWidth().height(BarHeight).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassBackButton(onClick = { nav.pop() })
                Text(
                    movie.title,
                    Modifier.weight(1f).padding(start = 14.dp).alpha(progress),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
