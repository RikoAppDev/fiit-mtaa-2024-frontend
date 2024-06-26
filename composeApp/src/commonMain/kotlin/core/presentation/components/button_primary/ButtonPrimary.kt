package core.presentation.components.button_primary

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import core.presentation.components.cicrular_progress.CustomCircularProgress
import ui.data.getButtonColors
import ui.domain.ButtonColors
import ui.domain.ColorVariation
import ui.theme.Shapes

@Composable
fun ButtonPrimary(
    type: ColorVariation,
    onClick: () -> Unit,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = Shapes.small,
    // Other default parameters can be added here
    text: String,
    isLoading: Boolean = false,
) {
    val isDarkMode = isSystemInDarkTheme()
    val colorCombination: ButtonColors = getButtonColors(type)


    Button(
        shape = Shapes.medium,
        onClick = { if (!isLoading) onClick() },
        modifier = buttonModifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorCombination.backgroundColor,
            contentColor = colorCombination.backgroundColor,
            disabledBackgroundColor = MaterialTheme.colors.surface,
            disabledContentColor  = MaterialTheme.colors.secondary
        ),
        enabled = enabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
    ) {
        if (isLoading) {
            CustomCircularProgress()
        } else {
            Text(
                text,
                modifier = textModifier.padding(8.dp),
                style = MaterialTheme.typography.button,
                color = colorCombination.textColor
            )
        }

    }

}