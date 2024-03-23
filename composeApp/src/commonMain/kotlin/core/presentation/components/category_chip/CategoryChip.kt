package core.presentation.components.category_chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.data.getButtonColors
import ui.domain.ColorVariation
import ui.theme.OnOrange
import ui.theme.Orange
import ui.theme.Shapes
import ui.theme.Typography

@Composable
fun CategoryChip(text: String, color: ColorVariation = ColorVariation.LIME) {
    val colorVariation = getButtonColors(color)
    Box(Modifier.clip(Shapes.large)) {
        Text(
            text = text,
            Modifier.background(colorVariation.backgroundColor).padding(16.dp, 8.dp),
            color = colorVariation.textColor,
            style = Typography.body2,
            fontWeight = FontWeight.SemiBold
        )
    }
}