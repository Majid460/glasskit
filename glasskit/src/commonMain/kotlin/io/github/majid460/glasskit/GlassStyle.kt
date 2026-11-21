package io.github.majid460.glasskit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas

/**
 * Frosted-glass look for Compose content: translucent gradient fill plus a light gradient edge.
 * This is a styling helper for panels inside your screens. The real system glass is the tab bar.
 */
public fun Modifier.glass(
    shape: Shape = RoundedCornerShape(24.dp),
    tint: Color = Color.White,
    fillAlpha: Float = 0.08f,
    borderAlpha: Float = 0.22f,
): Modifier = this
    .clip(shape)
    .background(Brush.verticalGradient(listOf(tint.copy(alpha = fillAlpha * 1.6f), tint.copy(alpha = fillAlpha * 0.6f))))
    .border(
        width = 1.dp,
        brush = Brush.verticalGradient(listOf(tint.copy(alpha = borderAlpha), tint.copy(alpha = borderAlpha * 0.2f))),
        shape = shape,
    )

/** Round glass back button for pushed screens (iOS has no system back button in Compose). */
@Composable
public fun GlassBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    tint: Color = Color.White,
) {
    Box(
        modifier = modifier.size(size).glass(CircleShape, fillAlpha = 0.16f).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(size * 0.38f)) {
            val w = this.size.width
            val h = this.size.height
            val stroke = 2.2.dp.toPx()
            drawLine(tint, Offset(w * 0.75f, h * 0.05f), Offset(w * 0.2f, h * 0.5f), stroke, StrokeCap.Round)
            drawLine(tint, Offset(w * 0.2f, h * 0.5f), Offset(w * 0.75f, h * 0.95f), stroke, StrokeCap.Round)
        }
    }
}
