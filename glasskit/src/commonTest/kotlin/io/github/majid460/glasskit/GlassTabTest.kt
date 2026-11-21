package io.github.majid460.glasskit

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GlassTabTest {
    private val icon = ImageVector.Builder(
        name = "empty",
        defaultWidth = androidx.compose.ui.unit.Dp(24f),
        defaultHeight = androidx.compose.ui.unit.Dp(24f),
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).build()

    @Test
    fun blankIdIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            GlassTab(id = " ", title = "Home", sfSymbol = "house", icon = icon)
        }
    }
}
