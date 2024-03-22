package auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import auth.presentation.register.component.RegisterStep1ScreenComponent
import auth.presentation.register.component.RegisterStep1ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonColorType
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.email
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.next_step
import grabit.composeapp.generated.resources.password
import grabit.composeapp.generated.resources.register_screen__has_account
import grabit.composeapp.generated.resources.repeat_password
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.OnOrange
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun RegisterStep1Screen(component: RegisterStep1ScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()
    val passwordRepeated by component.passwordRepeated.subscribeAsState()
    val passwordsMatch by component.passwordsMatch.subscribeAsState()
    val isValid by component.isValid.subscribeAsState()

    val FocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(40.dp).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            imageVector = vectorResource(Res.drawable.grabit),
            contentDescription = stringResource(Res.string.logo)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text("Create account", style = Typography.h2)
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledInput(
                value = email,
                onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdateEmail(it)) },
                label = stringResource(Res.string.email),
            )

            FilledInput(
                value = password,
                onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdatePassword(it)) },
                label = stringResource(Res.string.password),
                visualTransformation = PasswordVisualTransformation(),
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
            )

            ButtonPrimary(
                type = ButtonColorType.ORANGE,
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
                    style = Typography.body2
                )
                Spacer(Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString(stringResource(Res.string.login_screen__login)),
                    onClick = { component.onEvent(RegisterStep1ScreenEvent.GoBackToLogin) },
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