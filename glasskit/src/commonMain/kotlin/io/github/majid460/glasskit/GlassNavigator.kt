package io.github.majid460.glasskit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * One entry in a back stack. Plain data (route name + optional string argument),
 * so users never need to set up serialization or `NavKey` classes.
 */
public class GlassKey internal constructor(
    internal val id: String,
    public val tabId: String,
    public val route: String,
    public val arg: String?,
) {
    override fun equals(other: Any?): Boolean = other is GlassKey && other.id == id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = if (arg == null) route else "$route/$arg"
}

/**
 * Owns ALL navigation state: the selected tab and one back stack per tab.
 * Screens are addressed by name: `navigator.push("detail", arg = "42")`.
 */
@Stable
public class GlassNavigator internal constructor(
    private val tabIds: List<String>,
    startTabId: String,
    restored: List<String>? = null,
) {
    private var counter = 0
    private val stacks: Map<String, SnapshotStateList<GlassKey>> = tabIds.associateWith { mutableStateListOf() }

    /** Id of the selected tab. */
    public var selectedTabId: String by mutableStateOf(startTabId)
        private set

    internal var drawerRequest: ((Boolean) -> Unit)? = null

    init {
        if (restored == null || !restore(restored)) {
            stacks.values.forEach { it.clear() }
            tabIds.forEach { stacks.getValue(it).add(newKey(it, it, null)) }
        }
    }

    internal val currentStack: SnapshotStateList<GlassKey> get() = stacks.getValue(selectedTabId)

    internal fun stackFor(tabId: String): SnapshotStateList<GlassKey> = stacks.getValue(tabId)

    /** Route name of the screen currently on top of the selected tab. */
    public val currentRoute: String get() = currentStack.last().route

    /** True when the selected tab has a pushed screen above its root. */
    public val canPop: Boolean get() = currentStack.size > 1

    /** Readable stack of the selected tab, e.g. "home > detail/3 > detail/7". */
    public val backStackSummary: String get() = currentStack.joinToString(" > ")

    /** Switch tab. Re-selecting the current tab returns to its root screen. */
    public fun selectTab(tabId: String) {
        require(tabId in stacks) { "Unknown tab id: $tabId" }
        if (tabId == selectedTabId) popToRoot() else selectedTabId = tabId
    }

    /** Push a named screen onto the selected tab's stack. */
    public fun push(route: String, arg: String? = null) {
        currentStack.add(newKey(selectedTabId, route, arg))
    }

    /** Pop one screen. Returns false when already at the tab root. */
    public fun pop(): Boolean {
        val stack = currentStack
        if (stack.size <= 1) return false
        stack.removeAt(stack.lastIndex)
        return true
    }

    /** Pop everything above the tab root. */
    public fun popToRoot() {
        val stack = currentStack
        while (stack.size > 1) stack.removeAt(stack.lastIndex)
    }

    public fun openDrawer() { drawerRequest?.invoke(true) }
    public fun closeDrawer() { drawerRequest?.invoke(false) }

    private fun newKey(tabId: String, route: String, arg: String?) =
        GlassKey(id = "$tabId#${counter++}", tabId = tabId, route = route, arg = arg)

    // ---- state saving (survives Android configuration changes / process death) ----
    internal fun encode(): List<String> = buildList {
        add(selectedTabId)
        add(counter.toString())
        stacks.forEach { (_, stack) ->
            stack.forEach { k -> add(listOf(k.id, k.tabId, k.route, if (k.arg == null) "N" else "V" + k.arg).joinToString(SEP)) }
        }
    }

    private fun restore(data: List<String>): Boolean {
        if (data.size < 3) return false
        val selected = data[0]
        if (selected !in stacks) return false
        counter = data[1].toIntOrNull() ?: return false
        for (line in data.drop(2)) {
            val f = line.split(SEP, limit = 4)
            if (f.size != 4) return false
            val stack = stacks[f[1]] ?: return false
            stack.add(GlassKey(f[0], f[1], f[2], if (f[3].startsWith("V")) f[3].substring(1) else null))
        }
        if (stacks.values.any { it.isEmpty() }) return false
        selectedTabId = selected
        return true
    }

    internal companion object {
        const val SEP = "\u0001"
    }
}

/** Creates (and saves across configuration changes) a [GlassNavigator] for [tabs]. */
@Composable
public fun rememberGlassNavigator(
    tabs: List<GlassTab>,
    startTabId: String = tabs.first().id,
): GlassNavigator {
    val ids = tabs.map { it.id }
    return rememberSaveable(
        saver = Saver<GlassNavigator, List<String>>(
            save = { it.encode() },
            restore = { GlassNavigator(ids, startTabId, it) },
        ),
    ) { GlassNavigator(ids, startTabId) }
}
