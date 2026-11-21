package sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Palette {
    val Ink = Color(0xFF05060F)
    val Panel = Color(0xFF0B0D1C)
    val Cyan = Color(0xFF5CE1FF)
    val Violet = Color(0xFF9B7BFF)
    val Pink = Color(0xFFFF5CAA)
    val Text = Color(0xFFEAF0FF)
    val Muted = Color(0xFF8E97B8)
}

private val scheme = darkColorScheme(
    primary = Palette.Cyan,
    secondary = Palette.Violet,
    tertiary = Palette.Pink,
    background = Palette.Ink,
    surface = Palette.Panel,
    onBackground = Palette.Text,
    onSurface = Palette.Text,
    onPrimary = Palette.Ink,
)

@Composable
fun FuturisticTheme(content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = scheme, content = content)
