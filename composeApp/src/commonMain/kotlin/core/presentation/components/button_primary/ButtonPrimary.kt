package core.presentation.components.button_primary

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
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
import ui.theme.Shapes
import ui.theme.Typography


data class ButtonColors(val backgroundColor: Color, val textColor: Color)

fun getButtonColors(type: ButtonColorType): ButtonColors {
    return when (type) {
        ButtonColorType.CHERRY -> ButtonColors(Cherry, OnCherry)
        ButtonColorType.LEMON -> ButtonColors(Lemon, OnLemon)
        ButtonColorType.LIME -> ButtonColors(Lime, OnLime)
        ButtonColorType.APPLE -> ButtonColors(Apple, OnApple)
        ButtonColorType.ORANGE -> ButtonColors(Orange, OnOrange)
    }
}
@Composable
fun ButtonPrimary(
    type: ButtonColorType,
    onClick: () -> Unit,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = Shapes.small,
    // Other default parameters can be added here
    text:String
) {

    val colorCombination:ButtonColors = getButtonColors(type)

    Button(
        shape = Shapes.medium,
        onClick = onClick ,
        modifier = buttonModifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorCombination.backgroundColor,
            contentColor = OnOrange
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
    ) {
        Text(
            text,
            modifier = textModifier.padding(8.dp),
            style = Typography.button,
            color = colorCombination.textColor
        )
    }
    
}

enum class ButtonColorType {
    CHERRY,
    LEMON,
    LIME,
    APPLE,
    ORANGE
}