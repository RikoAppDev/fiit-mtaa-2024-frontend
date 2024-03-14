package auth.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import auth.presentation.register.component.RegisterStep3ScreenComponent
import auth.presentation.register.component.RegisterStep3ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ui.theme.Shapes

@Composable
fun RegisterStep3Screen(component: RegisterStep3ScreenComponent) {
    val name by component.name.subscribeAsState()
    val phone by component.phone.subscribeAsState()

    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Personal details", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { component.onEvent(RegisterStep3ScreenEvent.UpdateName(it)) },
            label = { Text("Name") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { component.onEvent(RegisterStep3ScreenEvent.UpdatePhone(it)) },
            label = { Text("Phone") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            shape = Shapes.large, onClick = {
                component.onEvent(RegisterStep3ScreenEvent.ClickButtonNext)
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create account", modifier = Modifier.padding(8.dp))
        }
    }
}