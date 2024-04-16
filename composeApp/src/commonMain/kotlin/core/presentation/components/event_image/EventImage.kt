package core.presentation.components.event_image

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_image
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EventImage(
    uri: String,
    alt: String? = stringResource(Res.string.event_image),
    height: Dp = 192.dp,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = Modifier.height(height).clip(Shapes.medium).then(modifier),
        model = uri,
        contentDescription = alt,
        imageLoader = ImageLoader(LocalPlatformContext.current),
        contentScale = ContentScale.Crop,
    )
}