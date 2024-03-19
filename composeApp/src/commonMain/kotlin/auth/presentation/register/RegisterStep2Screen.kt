package auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.domain.model.AccountType
import auth.presentation.register.component.RegisterStep1ScreenEvent
import auth.presentation.register.component.RegisterStep2ScreenComponent
import auth.presentation.register.component.RegisterStep2ScreenEvent
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
import navigation.RootComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.theme.OnOrange
import ui.theme.Orange
import ui.theme.SecondaryText
import ui.theme.Shapes
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegisterStep2Screen(component: RegisterStep2ScreenComponent) {
    val accountType by component.accountType.subscribeAsState()
    val radioButton = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text("Create account", style = Typography.h1)
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(12.dp))

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
                shape = Shapes.medium,
                onClick = {
                    component.onEvent(RegisterStep2ScreenEvent.OnNextStepButtonClick)
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
        }
    }
}