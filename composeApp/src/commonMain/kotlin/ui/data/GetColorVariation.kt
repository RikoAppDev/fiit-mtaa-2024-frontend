package ui.data

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ui.domain.ButtonColors
import ui.domain.ColorVariation
import ui.theme.DarkApple
import ui.theme.DarkCherry
import ui.theme.DarkLemon
import ui.theme.DarkLime
import ui.theme.DarkOnApple
import ui.theme.DarkOnCherry
import ui.theme.DarkOnLemon
import ui.theme.DarkOnLime
import ui.theme.DarkOnOrange
import ui.theme.DarkOrange
import ui.theme.LightApple
import ui.theme.LightCherry
import ui.theme.LightLemon
import ui.theme.LightLime
import ui.theme.LightOnApple
import ui.theme.LightOnCherry
import ui.theme.LightOnLemon
import ui.theme.LightOnLime
import ui.theme.LightOnOrange
import ui.theme.LightOrange

@Composable
fun getButtonColors(type: ColorVariation): ButtonColors {
    val isDarkMode = isSystemInDarkTheme()
    if (isDarkMode) {
        return when (type) {
            ColorVariation.CHERRY -> ButtonColors(DarkCherry, DarkOnCherry)
            ColorVariation.LEMON -> ButtonColors(DarkLemon, DarkOnLemon)
            ColorVariation.LIME -> ButtonColors(DarkLime, DarkOnLime)
            ColorVariation.APPLE -> ButtonColors(DarkApple, DarkOnApple)
            ColorVariation.ORANGE -> ButtonColors(DarkOrange, DarkOnOrange)
        }
    } else {
        return when (type) {
            ColorVariation.CHERRY -> ButtonColors(LightCherry, LightOnCherry)
            ColorVariation.LEMON -> ButtonColors(LightLemon, LightOnLemon)
            ColorVariation.LIME -> ButtonColors(LightLime, LightOnLime)
            ColorVariation.APPLE -> ButtonColors(LightApple, LightOnApple)
            ColorVariation.ORANGE -> ButtonColors(LightOrange, LightOnOrange)
        }
    }
}