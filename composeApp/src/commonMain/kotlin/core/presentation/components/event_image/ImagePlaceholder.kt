package core.presentation.components.event_image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_create_update_screen__pick_image
import grabit.composeapp.generated.resources.image_placeholder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ImagePlaceholder(
    height: Dp = 192.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(height)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colors.surface, Shapes.medium)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.image_placeholder),
            contentDescription = stringResource(Res.string.event_create_update_screen__pick_image)
        )
    }
}