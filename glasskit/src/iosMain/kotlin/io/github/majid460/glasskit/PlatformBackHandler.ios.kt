package io.github.majid460.glasskit

import androidx.compose.runtime.Composable

@Composable
internal actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS: pushed screens use GlassBackButton. (Navigation 3 mode gets the edge-swipe from NavDisplay.)
}
