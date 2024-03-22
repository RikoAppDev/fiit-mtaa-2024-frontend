package auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import auth.domain.model.AccountType
import auth.presentation.register.component.RegisterStep3ScreenComponent
import auth.presentation.register.component.RegisterStep3ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonColorType
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.company_name
import grabit.composeapp.generated.resources.next_step
import grabit.composeapp.generated.resources.phone_number
import grabit.composeapp.generated.resources.register_screen__company_details_title
import grabit.composeapp.generated.resources.register_screen__personal_details_title
import grabit.composeapp.generated.resources.your_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegisterStep3Screen(component: RegisterStep3ScreenComponent) {
    val name by component.name.subscribeAsState()
    val phone by component.phone.subscribeAsState()

    val accountType = component.newUser.accountType

    val roleBasedCopy = when (accountType) {
        AccountType.HARVESTER -> RoleBasedCopy(
            stringResource(Res.string.register_screen__personal_details_title),
            stringResource(Res.string.your_name)
        )

        AccountType.ORGANISER -> RoleBasedCopy(
            stringResource(Res.string.register_screen__company_details_title),
            stringResource(Res.string.company_name)
        )
    }

    Column(modifier = Modifier.padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                roleBasedCopy.title, style = Typography.h1
            )

            Spacer(modifier = Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilledInput(
                    value = name,
                    onValueChange = { component.onEvent(RegisterStep3ScreenEvent.UpdateName(it)) },
                    label = roleBasedCopy.name
                )

                FilledInput(
                    value = phone,
                    onValueChange = { component.onEvent(RegisterStep3ScreenEvent.UpdateName(it)) },
                    label = stringResource(Res.string.phone_number)
                )

            }
            Spacer(Modifier.height(32.dp))
            ButtonPrimary(
                type = ButtonColorType.ORANGE,
                onClick = { component.onEvent(RegisterStep3ScreenEvent.ClickButtonNext) },
                text = stringResource(Res.string.next_step)
            )

        }
    }
}

data class RoleBasedCopy(val title: String, val name: String)