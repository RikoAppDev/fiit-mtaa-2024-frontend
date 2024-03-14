package auth.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.login.component.LoginScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.Shapes

@Preview
@Composable
fun LoginScreen(component: LoginScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()
    val passwordHidden by component.passwordHidden.subscribeAsState()
    val scope = rememberCoroutineScope()


    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Login", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { component.onEvent(LoginScreenEvent.UpdateEmail(it)) },
            label = { Text("Email") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { component.onEvent(LoginScreenEvent.UpdatePassword(it)) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(passwordHidden)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Account does not exist?")
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                shape = Shapes.large,
                onClick = { component.onEvent(LoginScreenEvent.ClickRegisterButton) }
            ) { Text("Create account") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            shape = Shapes.large,
            onClick = { /*component.onEvent(LoginScreenEvent.ClickLoginButton)*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", modifier = Modifier.padding(8.dp))
        }
    }
}