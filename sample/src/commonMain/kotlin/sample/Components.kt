package sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.majid460.glasskit.glass

/** Height of the pinned top bar's content row (below the status bar). */
val BarHeight = 72.dp

@Composable
fun statusTop(): Dp = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

/**
 * 0..1 as a list scrolls away from its top. 0 = at rest (bar is transparent, part of the page),
 * 1 = scrolled (bar is pinned with a scrim and content passes under it).
 */
@Composable
fun LazyListState.scrollProgress(range: Float = 140f): Float {
    val state = this
    val p by remember(state) {
        derivedStateOf {
            if (state.firstVisibleItemIndex > 0) 1f
            else (state.firstVisibleItemScrollOffset / range).coerceIn(0f, 1f)
        }
    }
    return p
}

@Composable
fun LazyGridState.scrollProgress(range: Float = 140f): Float {
    val state = this
    val p by remember(state) {
        derivedStateOf {
            if (state.firstVisibleItemIndex > 0) 1f
            else (state.firstVisibleItemScrollOffset / range).coerceIn(0f, 1f)
        }
    }
    return p
}

/**
 * Pinned top area drawn OVER the scrolling content. Includes the status-bar inset.
 * [progress] fades a dark scrim in, so content scrolls under the bar and the clock never collides with it.
 */
@Composable
fun ScrollScrim(progress: Float, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier.fillMaxWidth().background(
            Brush.verticalGradient(
                0f to Palette.Ink.copy(alpha = 0.97f * progress),
                0.72f to Palette.Ink.copy(alpha = 0.88f * progress),
                1f to Color.Transparent,
            ),
        ),
    ) {
        Column(Modifier.statusBarsPadding()) { content() }
    }
}

@Composable
fun PosterCard(movie: Movie, onClick: () -> Unit, modifier: Modifier = Modifier, rank: Int? = null) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier
            .aspectRatio(2f / 3f)
            .clip(shape)
            .border(1.dp, Color.White.copy(alpha = 0.14f), shape)
            .clickable(onClick = onClick),
    ) {
        PosterImage(movie.id, Modifier.fillMaxSize())
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(0.55f to Color.Transparent, 1f to Palette.Ink.copy(alpha = 0.92f)),
            ),
        )
        if (rank != null) {
            Text(
                "$rank",
                Modifier.align(Alignment.TopStart).padding(start = 12.dp, top = 6.dp),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.92f),
            )
        }
        Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
            Text(movie.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color.White)
            Box(Modifier.size(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, null, Modifier.size(14.dp), tint = Color(0xFFFFD66B))
                Text(" ${movie.rating}  ·  ${movie.year}", style = MaterialTheme.typography.labelSmall, color = Color(0xFFC9D2F0))
            }
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val base = Modifier.clip(CircleShape).clickable(onClick = onClick)
    Box(
        if (selected) base.background(Brush.horizontalGradient(listOf(Palette.Cyan, Palette.Violet)), CircleShape)
        else base.glass(CircleShape, fillAlpha = 0.07f),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Palette.Ink else Palette.Text,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        title,
        modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun GlassIconButton(icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier.size(46.dp).glass(CircleShape, fillAlpha = 0.14f).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, null, tint = Palette.Text) }
}

@Composable
fun GradientButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector? = null) {
    Row(
        modifier
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(Palette.Cyan, Palette.Violet, Palette.Pink)))
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (icon != null) { Icon(icon, null, tint = Palette.Ink); Box(Modifier.width(6.dp)) }
        Text(label, color = Palette.Ink, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier.glass(RoundedCornerShape(22.dp), fillAlpha = 0.09f).padding(vertical = 16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = Palette.Cyan)
        Text(label, style = MaterialTheme.typography.labelMedium, color = Palette.Muted)
    }
}
