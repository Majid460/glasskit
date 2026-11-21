package io.github.majid460.glasskit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/** What a screen receives: the navigator, its optional argument, and the padding to apply under the tab bar. */
@Stable
public class GlassScreenContext internal constructor(
    public val navigator: GlassNavigator,
    public val arg: String?,
    public val contentPadding: PaddingValues,
)

/**
 * Registry of named screens. Each tab's root screen uses the tab id as its name.
 * ```
 * screens = {
 *     screen("home") { ctx -> Home(onOpen = { ctx.navigator.push("detail", it) }) }
 *     screen("detail") { ctx -> Detail(id = ctx.arg) }
 * }
 * ```
 */
public class GlassScreens internal constructor() {
    internal val registry: MutableMap<String, @Composable (GlassScreenContext) -> Unit> = LinkedHashMap()

    public fun screen(route: String, content: @Composable (GlassScreenContext) -> Unit) {
        registry[route] = content
    }
}
