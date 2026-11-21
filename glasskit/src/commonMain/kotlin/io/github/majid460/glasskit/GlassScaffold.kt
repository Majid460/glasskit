package io.github.majid460.glasskit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

/**
 * Tab scaffold. On iOS the bar is a real `UITabBar` (Liquid Glass on iOS 26 when built with the iOS 26 SDK).
 * Elsewhere it is a Material 3 `NavigationBar`.
 *
 * The selected tab lives in YOUR code: pass [selectedTabId] and handle [onTabSelected].
 *
 * @param barVisible hide the bar (e.g. while a drawer is open: a native iOS bar would draw above Compose overlays).
 * @param content screen content. [PaddingValues] reserves the space covered by the tab bar.
 */
@Composable
public fun GlassScaffold(
    tabs: List<GlassTab>,
    selectedTabId: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    barVisible: Boolean = true,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    require(tabs.isNotEmpty()) { "GlassScaffold needs at least one tab" }
    var barHeightPx by remember { mutableIntStateOf(0) }
    val barHeight = with(LocalDensity.current) { barHeightPx.toDp() }

    Box(modifier.fillMaxSize()) {
        content(PaddingValues(bottom = barHeight))
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .onSizeChanged { if (it.height > 0) barHeightPx = it.height },
        ) {
            if (barVisible) GlassTabBar(tabs, selectedTabId, onTabSelected)
        }
    }
}
