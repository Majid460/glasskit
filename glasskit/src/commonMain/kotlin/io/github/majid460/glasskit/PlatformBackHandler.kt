package io.github.majid460.glasskit

import androidx.compose.runtime.Composable

/** System back for the non-Navigation-3 host. Android: BackHandler. iOS: no-op (use [GlassBackButton]). */
@Composable
internal expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)
