package navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.presentation.components.logo.GrabItLogo
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CustomTopBar(
    onProfileIconClick: () -> Unit
) {
    val topBarModifier = if (isSystemInDarkTheme()) {
        Modifier.background(MaterialTheme.colors.background).displayCutoutPadding().height(80.dp)
    } else {
        Modifier.background(Color.White).displayCutoutPadding().height(80.dp).shadow(
            elevation = 16.dp, spotColor = Color(0x40E9E9E9), ambientColor = Color(0x40E9E9E9)
        )
    }

    TopAppBar(
        modifier = topBarModifier,
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.width(120.dp)) {
                GrabItLogo()
            }
            IconButton(
                onClick = {
                    onProfileIconClick()
                },
                modifier = Modifier.clip(CircleShape).background(MaterialTheme.colors.surface)
                    .size(size = 32.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = vectorResource(Res.drawable.profile),
                    contentDescription = stringResource(Res.string.logo),
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}