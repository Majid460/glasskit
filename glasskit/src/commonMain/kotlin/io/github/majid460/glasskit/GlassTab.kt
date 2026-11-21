package io.github.majid460.glasskit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One entry in the tab bar.
 *
 * @property id stable identifier. Your own navigation layer owns the selected id; GlassKit never stores it.
 * @property title label shown under the icon.
 * @property sfSymbol SF Symbol name used by the native iOS bar (e.g. "house.fill").
 * @property icon vector used by the Compose fallback bar.
 */
@Immutable
public data class GlassTab(
    public val id: String,
    public val title: String,
    public val sfSymbol: String,
    public val icon: ImageVector,
) {
    init {
        require(id.isNotBlank()) { "GlassTab.id must not be blank" }
    }
}
