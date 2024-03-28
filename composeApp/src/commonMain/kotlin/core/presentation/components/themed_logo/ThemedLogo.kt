package core.presentation.components.themed_logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.logo_dark
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ThemedLogo(
    modifier:Modifier = Modifier
) {
    val isDarkMode = isSystemInDarkTheme()
    val logo =
        if (isDarkMode)
            vectorResource(Res.drawable.logo_dark)
        else
            vectorResource(Res.drawable.grabit)

    Image(
        modifier = modifier,
        imageVector = logo,
        contentDescription = stringResource(Res.string.logo)
    )

}