package io.github.majid460.glasskit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch

/**
 * The whole app shell: tab bar + per-tab back stacks + optional side drawer.
 * The library manages ALL navigation state. You only name your screens.
 *
 * @param useNavigation3 true: screens are shown by Navigation 3 (`NavDisplay`) with its transitions and
 *   back handling. false: a lightweight built-in host is used (no Navigation 3 involved at runtime).
 *   Back stacks live in [navigator], so you can flip this flag at runtime without losing state.
 * @param drawerContent optional modal drawer. Receives the [navigator] (use `closeDrawer()` after navigating).
 *   The edge-swipe opens it only on a tab's root screen so it never fights the iOS back swipe.
 * @param screens register screens by name. Each tab's root screen is named after the tab id.
 */
@Composable
public fun GlassApp(
    tabs: List<GlassTab>,
    modifier: Modifier = Modifier,
    startTabId: String = tabs.first().id,
    useNavigation3: Boolean = true,
    navigator: GlassNavigator = rememberGlassNavigator(tabs, startTabId),
    drawerContent: (@Composable ColumnScope.(GlassNavigator) -> Unit)? = null,
    screens: GlassScreens.() -> Unit,
) {
    val registry = GlassScreens().apply(screens)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    DisposableEffect(navigator, drawerState) {
        navigator.drawerRequest = { open ->
            scope.launch { if (open) drawerState.open() else drawerState.close() }
        }
        onDispose { navigator.drawerRequest = null }
    }

    val body: @Composable () -> Unit = {
        GlassScaffold(
            tabs = tabs,
            selectedTabId = navigator.selectedTabId,
            onTabSelected = navigator::selectTab,
            modifier = modifier,
            barVisible = drawerState.targetValue == DrawerValue.Closed,
        ) { padding ->
            TabContent(navigator, registry, padding, useNavigation3)
        }
    }

    if (drawerContent == null) {
        body()
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !navigator.canPop,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)) {
                    drawerContent.invoke(this, navigator)
                }
            },
            content = body,
        )
    }
}

/**
 * Switching tabs cross-fades between tab hosts (instead of animating like a push), and each tab's
 * UI state (scroll positions, text fields) is kept by a saveable-state holder keyed by tab id.
 */
@Composable
private fun TabContent(
    navigator: GlassNavigator,
    registry: GlassScreens,
    padding: PaddingValues,
    useNavigation3: Boolean,
) {
    val holder = rememberSaveableStateHolder()
    Crossfade(
        targetState = navigator.selectedTabId,
        animationSpec = tween(200),
        label = "glass-tabs",
    ) { tabId ->
        holder.SaveableStateProvider(tabId) {
            val stack = navigator.stackFor(tabId)
            if (useNavigation3) Nav3Host(navigator, stack, registry, padding)
            else SimpleHost(navigator, stack, registry, padding)
        }
    }
}

@Composable
private fun Nav3Host(
    navigator: GlassNavigator,
    stack: SnapshotStateList<GlassKey>,
    registry: GlassScreens,
    padding: PaddingValues,
) {
    NavDisplay(
        backStack = stack,
        onBack = { navigator.pop() },
        entryProvider = { key ->
            NavEntry(key, contentKey = key.id) {
                ScreenFor(key, navigator, registry, padding)
            }
        },
    )
}

@Composable
private fun SimpleHost(
    navigator: GlassNavigator,
    stack: SnapshotStateList<GlassKey>,
    registry: GlassScreens,
    padding: PaddingValues,
) {
    val holder = rememberSaveableStateHolder()
    PlatformBackHandler(enabled = stack.size > 1 && navigator.currentStack === stack) { navigator.pop() }
    AnimatedContent(
        targetState = stack.last(),
        transitionSpec = {
            (fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 10 }) togetherWith fadeOut(tween(140))
        },
        label = "glass-simple-host",
    ) { key ->
        holder.SaveableStateProvider(key.id) {
            ScreenFor(key, navigator, registry, padding)
        }
    }
}

@Composable
private fun ScreenFor(key: GlassKey, navigator: GlassNavigator, registry: GlassScreens, padding: PaddingValues) {
    val screen = registry.registry[key.route]
    if (screen != null) {
        screen(GlassScreenContext(navigator, key.arg, padding))
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No screen registered for \"${key.route}\"")
        }
    }
}
