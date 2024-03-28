package auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.login.component.LoginScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import core.presentation.components.themed_logo.ThemedLogo
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
import ui.theme.GrabItTheme
import ui.theme.OnOrange

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun LoginScreen(component: LoginScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxHeight().background(MaterialTheme.colors.background).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        ThemedLogo()
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            color = MaterialTheme.colors.onBackground,
            text = stringResource(Res.string.login_screen__login),
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Error will be displayed here",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledInput(
                value = email,
                onValueChange = { component.onEvent(LoginScreenEvent.UpdateEmail(it)) },
                label = stringResource(Res.string.email),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            )
            FilledInput(
                value = password,
                onValueChange = { component.onEvent(LoginScreenEvent.UpdatePassword(it)) },
                label = stringResource(Res.string.password),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                }
            )
            ButtonPrimary(
                type = ColorVariation.ORANGE,
                onClick = { component.onEvent(LoginScreenEvent.ClickLoginButton) },
                text = stringResource(Res.string.login_screen__login)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.login_screen__no_account),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString(stringResource(Res.string.login_screen__create_account)),
                    onClick = { component.onEvent(LoginScreenEvent.ClickRegisterButton) },
                    style = TextStyle(
                        color = OnOrange,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight
                    ),
                )
            }
        }
    }
}