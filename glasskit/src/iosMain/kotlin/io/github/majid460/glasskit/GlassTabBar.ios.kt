@file:OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)

package io.github.majid460.glasskit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarAppearance
import platform.UIKit.UITabBarDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.darwin.NSObject

/** Receives taps from the UITabBar and forwards the tab id to Compose. */
private class TabBarDelegate : NSObject(), UITabBarDelegateProtocol {
    var ids: List<String> = emptyList()
    var onSelect: (String) -> Unit = {}

    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        ids.getOrNull(didSelectItem.tag.toInt())?.let(onSelect)
    }
}

@Composable
internal actual fun GlassTabBar(
    tabs: List<GlassTab>,
    selectedTabId: String,
    onTabSelected: (String) -> Unit,
) {
    val currentOnSelected by rememberUpdatedState(onTabSelected)
    val delegate = remember { TabBarDelegate() }
    // Real height asked from UIKit. It includes the home-indicator safe area because the bar is
    // pinned to the very bottom of the screen (no spacer below it), so UIKit lays the pill out itself.
    var barHeight by remember { mutableStateOf(FALLBACK_HEIGHT.dp) }

    UIKitView(
        factory = {
            // Transparent bar background: only the system-drawn pill remains, so content shows through it.
            val appearance = UITabBarAppearance().apply { configureWithTransparentBackground() }
            UITabBar().apply {
                this.delegate = delegate
                standardAppearance = appearance
                scrollEdgeAppearance = appearance
                backgroundColor = UIColor.clearColor
                opaque = false
            }
        },
        modifier = Modifier.fillMaxWidth().height(barHeight),
        update = { bar ->
            delegate.ids = tabs.map { it.id }
            delegate.onSelect = { currentOnSelected(it) }
            val items = tabs.mapIndexed { index, tab ->
                UITabBarItem(
                    title = tab.title,
                    image = UIImage.systemImageNamed(tab.sfSymbol),
                    tag = index.toLong(),
                )
            }
            bar.setItems(items, animated = false)
            bar.selectedItem = items.getOrNull(tabs.indexOfFirst { it.id == selectedTabId })

            val fitted = bar.sizeThatFits(CGSizeMake(0.0, 0.0)).useContents { height }
            if (fitted > 0.0) barHeight = fitted.toFloat().dp
        },
        onRelease = { it.delegate = null },
        // KEY FIX: by default Compose draws ABOVE native views, leaving the window's black background behind
        // the bar. As an overlay the real UITabBar is drawn ON TOP of the Compose content, so its Liquid Glass
        // samples and blurs the content scrolling underneath it.
        properties = UIKitInteropProperties(placedAsOverlay = true),
    )
}

// Used until UIKit reports the real height (classic 49pt bar + typical 34pt home indicator).
private const val FALLBACK_HEIGHT = 83
