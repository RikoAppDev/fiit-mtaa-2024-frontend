package auth.presentation.register

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.domain.model.AccountType
import auth.presentation.register.component.RegisterStep2ScreenComponent
import auth.presentation.register.component.RegisterStep2ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ui.theme.Shapes

@Composable
fun RegisterStep2Screen(component: RegisterStep2ScreenComponent) {
    val accountType by component.accountType.subscribeAsState()
    val radioButton = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Choose account type", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        RadioButton(
            selected = accountType == AccountType.WORKER,
            onClick = {
                component.onEvent(RegisterStep2ScreenEvent.UpdateAccountType(AccountType.WORKER))
            },
            interactionSource = radioButton
        )
        RadioButton(
            selected = accountType == AccountType.ORGANISATION,
            onClick = {
                component.onEvent(RegisterStep2ScreenEvent.UpdateAccountType(AccountType.ORGANISATION))
            },
            interactionSource = radioButton
        )
        Button(
            shape = Shapes.large,
            onClick = {
                component.onEvent(RegisterStep2ScreenEvent.OnNextStepButtonClick)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next step", modifier = Modifier.padding(8.dp))
        }

    }
}