package io.github.majid460.glasskit

import androidx.compose.runtime.Composable

/** Platform tab bar. Implemented in `androidMain` (Material 3) and `iosMain` (UITabBar). Not public API. */
@Composable
internal expect fun GlassTabBar(
    tabs: List<GlassTab>,
    selectedTabId: String,
    onTabSelected: (String) -> Unit,
)
