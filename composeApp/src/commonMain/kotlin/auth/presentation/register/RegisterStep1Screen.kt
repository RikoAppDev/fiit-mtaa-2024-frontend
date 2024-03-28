package auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.unit.dp
import auth.presentation.register.component.RegisterStep1ScreenComponent
import auth.presentation.register.component.RegisterStep1ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import core.presentation.components.themed_logo.ThemedLogo
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.create_account
import grabit.composeapp.generated.resources.email
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.next_step
import grabit.composeapp.generated.resources.password
import grabit.composeapp.generated.resources.register_screen__has_account
import grabit.composeapp.generated.resources.repeat_password
import navigation.RootComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.domain.ColorVariation
import ui.theme.LightOnOrange

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun RegisterStep1Screen(component: RegisterStep1ScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()
    val passwordRepeated by component.passwordRepeated.subscribeAsState()
    val passwordsMatch by component.passwordsMatch.subscribeAsState()
    val isValid by component.isValid.subscribeAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxHeight().background(MaterialTheme.colors.background)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        ThemedLogo()
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            stringResource(Res.string.create_account),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledInput(
                value = email,
                onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdateEmail(it)) },
                label = stringResource(Res.string.email),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            FilledInput(
                value = password,
                onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdatePassword(it)) },
                label = stringResource(Res.string.password),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )
            FilledInput(
                value = passwordRepeated,
                onValueChange = {
                    component.onEvent(
                        RegisterStep1ScreenEvent.UpdatePasswordRepeated(
                            it
                        )
                    )
                },
                label = stringResource(Res.string.repeat_password),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                }
            )
            ButtonPrimary(
                type = ColorVariation.ORANGE,
                onClick = {
                    component.onEvent(RegisterStep1ScreenEvent.ClickButtonNext)
                },
                text = stringResource(Res.string.next_step)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(
                        Res.string.register_screen__has_account
                    ),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString(stringResource(Res.string.login_screen__login)),
                    onClick = { component.onEvent(RegisterStep1ScreenEvent.GoBackToLogin) },
                    style = TextStyle(
                        color = LightOnOrange,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        fontWeight = MaterialTheme.typography.body2.fontWeight
                    ),
                )
            }
        }
    }
}