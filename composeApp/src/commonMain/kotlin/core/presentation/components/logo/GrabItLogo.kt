package core.presentation.components.logo

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GrabItLogo(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        imageVector = vectorResource(Res.drawable.grabit),
        contentDescription = stringResource(Res.string.logo)
    )
}