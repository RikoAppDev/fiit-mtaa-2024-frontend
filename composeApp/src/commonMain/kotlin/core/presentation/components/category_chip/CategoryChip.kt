package core.presentation.components.category_chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_create_update_screen__remove_category
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.data.getButtonColors
import ui.domain.ColorVariation
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CategoryChip(
    text: String, color: ColorVariation = ColorVariation.LIME,
    removable: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body2,
) {
    val colorVariation = getButtonColors(color)

    BadgedBox(badge = {
        if (removable) {
            Badge(modifier = Modifier.clickable(enabled = removable) {
                if (onClick != null) {
                    onClick()
                }
            }.background(Color.Red, CircleShape).clip(CircleShape).size(24.dp)) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(Res.string.event_create_update_screen__remove_category),
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }
    }) {
        Box(Modifier.clip(Shapes.large)) {
            Text(
                text = text,
                modifier = Modifier.background(colorVariation.backgroundColor).padding(16.dp, 6.dp).then(modifier),
                color = colorVariation.textColor,
                style = style,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}