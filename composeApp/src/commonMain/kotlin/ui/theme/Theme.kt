package ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = darkPrimary,
    primaryVariant = darkPrimaryVariant,
    secondary = darkSecondary, //text color secondary
    background = darkBackground, // primary background
    surface = darkSurface, //elevated element - header, card,
    onSurface = darkOnSurface, //Primary text color
    error = darkError
)

private val LightColorPalette = lightColors(
    primary = lightPrimary,
    primaryVariant = lightPrimaryVariant,
    secondary = lightSecondary, //text color secondary
    background = lightBackground, // primary background
    surface = lightSurface, //elevated element - header, card,
    onSurface = lightOnSurface, //Primary text color
    error = lightError,
)

@Composable
fun GrabItTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = CustomTypography(),
        shapes = Shapes,
        content = content,
    )
}