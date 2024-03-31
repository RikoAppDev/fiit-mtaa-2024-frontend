package auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auth.presentation.register.component.RegisterStepFinalScreenComponent
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.logo.GrabItLogo
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.register_screen__collecting_adventures
import grabit.composeapp.generated.resources.register_screen__start_using
import grabit.composeapp.generated.resources.register_screen__welcome
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.domain.ColorVariation

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegisterStepFinalScreen(component: RegisterStepFinalScreenComponent) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background).padding(40.dp)
    ) {
        GrabItLogo()
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(Res.string.register_screen__welcome),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.register_screen__collecting_adventures),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(48.dp))

        ButtonPrimary(
            ColorVariation.LIME,
            onClick = { component.startUsingApp() },
            text = stringResource(Res.string.register_screen__start_using)
        )
    }
}