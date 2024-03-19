package auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.email
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.login_screen__logo
import grabit.composeapp.generated.resources.next_step
import grabit.composeapp.generated.resources.password
import grabit.composeapp.generated.resources.register_screen__has_account
import grabit.composeapp.generated.resources.repeat_password
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.OnOrange
import ui.theme.Orange
import ui.theme.SecondaryText
import ui.theme.Shapes
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
        modifier = Modifier.padding(24.dp).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            imageVector = vectorResource(Res.drawable.grabit),
            contentDescription = stringResource(Res.string.login_screen__logo),
            modifier = Modifier.width(167.dp).height(40.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text("Create account", style = Typography.h2)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdateEmail(it)) },
            label = { Text(stringResource(Res.string.email)) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OnOrange,
                cursorColor = OnOrange,
                focusedLabelColor = OnOrange,
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdatePassword(it)) },
            label = { Text(stringResource(Res.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OnOrange, cursorColor = OnOrange, focusedLabelColor = OnOrange
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            value = passwordRepeated,
            onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdatePasswordRepeated(it)) },
            label = { Text(stringResource(Res.string.repeat_password)) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OnOrange,
                cursorColor = OnOrange,
                focusedLabelColor = OnOrange,
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            shape = Shapes.medium,
            enabled = isValid,
            onClick = {
                component.onEvent(RegisterStep1ScreenEvent.ClickButtonNext)
            },
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Orange,
                contentColor = OnOrange,
            ),

            ) {
            Text(stringResource(Res.string.next_step), modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(
                    Res.string.register_screen__has_account
                ), style = Typography.body1, color = SecondaryText
            )
            Spacer(Modifier.width(4.dp))
            ClickableText(
                text = AnnotatedString(stringResource(Res.string.login_screen__login)),
                onClick = { component.onEvent(RegisterStep1ScreenEvent.GoBackToLogin) },
                style = TextStyle(
                    color = OnOrange,
                    fontSize = Typography.body1.fontSize
                ),
            )
        }
    }
}