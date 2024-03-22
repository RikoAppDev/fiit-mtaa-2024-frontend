package auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auth.presentation.register.component.RegisterStepFinalScreenComponent
import core.presentation.components.button_primary.ButtonColorType
import core.presentation.components.button_primary.ButtonPrimary
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.register_screen__collecting_adventures
import grabit.composeapp.generated.resources.register_screen__start_using
import grabit.composeapp.generated.resources.register_screen__welcome
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegisterStepFinalScreen(component: RegisterStepFinalScreenComponent) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(40.dp)
    ) {
        Image(
            imageVector = vectorResource(Res.drawable.grabit),
            contentDescription = stringResource(Res.string.logo)
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(Res.string.register_screen__welcome),
            style = Typography.h1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.register_screen__collecting_adventures),
            style = Typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        ButtonPrimary(
            ButtonColorType.LIME,
            onClick = { component.startUsingApp() },
            text = stringResource(Res.string.register_screen__start_using)
        )
    }
}