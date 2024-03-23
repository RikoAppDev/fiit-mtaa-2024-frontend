package ui.data

import ui.domain.ButtonColors
import ui.domain.ColorVariation
import ui.theme.Apple
import ui.theme.Cherry
import ui.theme.Lemon
import ui.theme.Lime
import ui.theme.OnApple
import ui.theme.OnCherry
import ui.theme.OnLemon
import ui.theme.OnLime
import ui.theme.OnOrange
import ui.theme.Orange

fun getButtonColors(type: ColorVariation): ButtonColors {
    return when (type) {
        ColorVariation.CHERRY -> ButtonColors(Cherry, OnCherry)
        ColorVariation.LEMON -> ButtonColors(Lemon, OnLemon)
        ColorVariation.LIME -> ButtonColors(Lime, OnLime)
        ColorVariation.APPLE -> ButtonColors(Apple, OnApple)
        ColorVariation.ORANGE -> ButtonColors(Orange, OnOrange)
    }
}