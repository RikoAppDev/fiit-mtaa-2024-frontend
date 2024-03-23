package auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.login.component.LoginScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.email
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.login_screen__create_account
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.login_screen__no_account
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.password
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.domain.ColorVariation
import ui.theme.OnOrange
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun LoginScreen(component: LoginScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()
    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            imageVector = vectorResource(Res.drawable.grabit),
            contentDescription = stringResource(Res.string.logo)
        )
        Spacer(modifier = Modifier.height(48.dp))

        Text(stringResource(Res.string.login_screen__login), style = Typography.h2)
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Error will be displayed here",
            style = Typography.body1,
            color = Color.Red
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledInput(
                value = email,
                onValueChange = { component.onEvent(LoginScreenEvent.UpdateEmail(it)) },
                label = stringResource(Res.string.email),
            )

            FilledInput(
                value = password,
                onValueChange = { component.onEvent(LoginScreenEvent.UpdatePassword(it)) },
                label = stringResource(Res.string.password),
                visualTransformation = PasswordVisualTransformation()
            )

            ButtonPrimary(
                type = ColorVariation.ORANGE,
                onClick = { /*component.onEvent(LoginScreenEvent.ClickLoginButton)*/ },
                text = stringResource(Res.string.login_screen__login)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(Res.string.login_screen__no_account),
                    style = Typography.body2
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString(stringResource(Res.string.login_screen__create_account)),
                    onClick = { component.onEvent(LoginScreenEvent.ClickRegisterButton) },
                    style = TextStyle(
                        color = OnOrange,
                        fontSize = Typography.body2.fontSize,
                        fontWeight = Typography.body2.fontWeight
                    ),
                )
            }
        }
    }


}