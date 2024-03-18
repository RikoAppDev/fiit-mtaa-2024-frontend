package auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.login.component.LoginScreenEvent
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.email
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.grabit_logo
import grabit.composeapp.generated.resources.login_screen__create_account
import grabit.composeapp.generated.resources.login_screen__email
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.login_screen__logo
import grabit.composeapp.generated.resources.login_screen__no_account
import grabit.composeapp.generated.resources.login_screen__password
import grabit.composeapp.generated.resources.password
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.OnOrange
import ui.theme.Orange
import ui.theme.Shapes
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun LoginScreen(component: LoginScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()

    Column(modifier = Modifier.padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(120.dp))
        Image(
            imageVector = vectorResource(Res.drawable.grabit),
            contentDescription = stringResource(Res.string.login_screen__logo)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(stringResource(Res.string.login_screen__login), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { component.onEvent(LoginScreenEvent.UpdateEmail(it)) },
            label = { Text(stringResource(Res.string.email)) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OnOrange,
                cursorColor = OnOrange,
                focusedLabelColor = OnOrange
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { component.onEvent(LoginScreenEvent.UpdatePassword(it)) },
            label = { Text(stringResource(Res.string.password)) },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = OnOrange,
                cursorColor = OnOrange,
                focusedLabelColor = OnOrange
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            shape = Shapes.medium,
            onClick = { /*component.onEvent(LoginScreenEvent.ClickLoginButton)*/ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Orange,
                contentColor = OnOrange
            )
        ) {
            Text(
                stringResource(Res.string.login_screen__login),
                modifier = Modifier.padding(8.dp),
                fontSize = Typography.button.fontSize
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(Res.string.login_screen__no_account),
                fontSize = Typography.caption.fontSize
            )
            Spacer(modifier = Modifier.width(12.dp))
            ClickableText(
                text = AnnotatedString(stringResource(Res.string.login_screen__create_account)),
                onClick = { component.onEvent(LoginScreenEvent.ClickRegisterButton) },
                style = TextStyle(
                    color = OnOrange,
                    fontSize = Typography.caption.fontSize
                ),
            )
        }
    }
}