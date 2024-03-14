package auth.presentation.register

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.register.component.RegisterStep1ScreenComponent
import auth.presentation.register.component.RegisterStep1ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.Shapes

@Preview
@Composable
fun RegisterStep1Screen(component: RegisterStep1ScreenComponent) {
    val email by component.email.subscribeAsState()
    val password by component.password.subscribeAsState()
    val passwordRepeated by component.passwordRepeated.subscribeAsState()

    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Create account", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdateEmail(it)) },
            label = { Text("Email") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdatePassword(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordRepeated,
            onValueChange = { component.onEvent(RegisterStep1ScreenEvent.UpdatePasswordRepeated(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            shape = Shapes.large,
            onClick = {
                component.onEvent(RegisterStep1ScreenEvent.ClickButtonNext)
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next step", modifier = Modifier.padding(8.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already have account?")
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                shape = Shapes.large,
                onClick = {
                    component.onEvent(RegisterStep1ScreenEvent.GoBackToLogin)
                }
            )
            { Text("Login") }
        }
    }
}