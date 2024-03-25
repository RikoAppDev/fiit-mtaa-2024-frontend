package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import coil3.compose.LocalPlatformContext
import grabit.composeapp.generated.resources.Nunito_Bold
import grabit.composeapp.generated.resources.Nunito_Regular
import grabit.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

private val DarkColorPalette = darkColors(
    primary = Orange,
    primaryVariant = OnOrange,
    secondary = Color.Red,
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
)

private val LightColorPalette = lightColors(
    primary = Orange,
    primaryVariant = OnOrange,
    secondary = Color.Red,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,

    /* Other default colors to override
    surface = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun CustomTypography() = Typography(
    defaultFontFamily = FontFamily(
        Font(Res.font.Nunito_Regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.Nunito_Bold, FontWeight.Bold, FontStyle.Normal)
    ),
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
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}