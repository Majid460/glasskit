package sample

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.min
import kotlin.random.Random

private class ArtPalette(val a: Color, val b: Color, val c: Color)

private val palettes = listOf(
    ArtPalette(Color(0xFF5CE1FF), Color(0xFF9B7BFF), Color(0xFF1B2A6B)),
    ArtPalette(Color(0xFFFF5CAA), Color(0xFFFFB86B), Color(0xFF4A1A5E)),
    ArtPalette(Color(0xFF7CFFB2), Color(0xFF3DD6FF), Color(0xFF0E3B4A)),
    ArtPalette(Color(0xFFB69CFF), Color(0xFFFF6FD8), Color(0xFF2A1B6B)),
    ArtPalette(Color(0xFFFFD66B), Color(0xFFFF6B6B), Color(0xFF5A1F2E)),
    ArtPalette(Color(0xFF6BA8FF), Color(0xFF7CFFE9), Color(0xFF12305E)),
)

/**
 * Procedurally generated "poster" artwork: glowing orbs, a moon, stars and a perspective grid.
 * Same [seed] gives the same picture on Android and iOS, with no image files or network.
 */
@Composable
fun GenerativeArt(seed: Int, modifier: Modifier = Modifier) {
    val p = palettes[abs(seed) % palettes.size]
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val rnd = Random(seed * 7919 + 13)

        drawRect(
            Brush.linearGradient(
                listOf(Palette.Ink, p.c.copy(alpha = 0.9f), Palette.Ink),
                start = Offset(0f, 0f),
                end = Offset(w, h),
            ),
        )

        val colors = listOf(p.a, p.b, p.c, p.a)
        repeat(4) { i ->
            val center = Offset(rnd.nextFloat() * w, rnd.nextFloat() * h * 0.8f)
            val radius = min(w, h) * (0.45f + rnd.nextFloat() * 0.6f)
            drawCircle(
                brush = Brush.radialGradient(listOf(colors[i].copy(alpha = 0.55f), Color.Transparent), center, radius),
                radius = radius,
                center = center,
            )
        }

        val moon = Offset(w * (0.3f + rnd.nextFloat() * 0.4f), h * (0.22f + rnd.nextFloat() * 0.16f))
        val moonR = min(w, h) * 0.14f
        drawCircle(
            brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.45f), Color.Transparent), moon, moonR * 3f),
            radius = moonR * 3f,
            center = moon,
        )
        drawCircle(
            brush = Brush.linearGradient(listOf(Color.White, p.a), moon - Offset(moonR, moonR), moon + Offset(moonR, moonR)),
            radius = moonR,
            center = moon,
        )
        drawCircle(Color.White.copy(alpha = 0.35f), moonR * 1.5f, moon, style = Stroke(1.dp.toPx()))

        repeat(46) {
            drawCircle(
                Color.White.copy(alpha = 0.25f + rnd.nextFloat() * 0.65f),
                radius = 0.6f + rnd.nextFloat() * 1.6f,
                center = Offset(rnd.nextFloat() * w, rnd.nextFloat() * h * 0.62f),
            )
        }

        val horizon = h * 0.64f
        for (j in 1..7) {
            val t = j / 7f
            val y = horizon + (h - horizon) * t * t
            drawLine(p.a.copy(alpha = 0.10f + 0.25f * t), Offset(0f, y), Offset(w, y), 1.dp.toPx())
        }
        for (i in -6..6) {
            drawLine(
                p.a.copy(alpha = 0.22f),
                Offset(w / 2 + i * w * 0.03f, horizon),
                Offset(w / 2 + i * w * 0.34f, h),
                1.dp.toPx(),
            )
        }
        drawLine(p.b.copy(alpha = 0.8f), Offset(0f, horizon), Offset(w, horizon), 1.5.dp.toPx())
    }
}
