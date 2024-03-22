package auth.presentation.register

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auth.domain.model.AccountType
import auth.presentation.register.component.RegisterStep2ScreenComponent
import auth.presentation.register.component.RegisterStep2ScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonColorType
import core.presentation.components.button_primary.ButtonPrimary
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.next_step
import grabit.composeapp.generated.resources.register_screen__choose_role_title
import grabit.composeapp.generated.resources.register_screen__role_harvester_text
import grabit.composeapp.generated.resources.register_screen__role_harvester_title
import grabit.composeapp.generated.resources.register_screen__role_organiser_text
import grabit.composeapp.generated.resources.register_screen__role_organiser_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.Apple
import ui.theme.LightGrey
import ui.theme.OnApple
import ui.theme.Shapes
import ui.theme.Typography

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegisterStep2Screen(component: RegisterStep2ScreenComponent) {
    val accountType by component.accountType.subscribeAsState()
    val radioButton = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                stringResource(Res.string.register_screen__choose_role_title), style = Typography.h1
            )
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AccountBox(
                    isSelected = accountType == AccountType.HARVESTER,
                    onClick = {
                        component.onEvent(
                            RegisterStep2ScreenEvent.UpdateAccountType(
                                AccountType.HARVESTER
                            )
                        )
                    },
                    roleName = stringResource(Res.string.register_screen__role_harvester_title),
                    roleDescription = stringResource(Res.string.register_screen__role_harvester_text),

                    )

                AccountBox(
                    isSelected = accountType == AccountType.ORGANISER,
                    onClick = {
                        component.onEvent(
                            RegisterStep2ScreenEvent.UpdateAccountType(
                                AccountType.ORGANISER
                            )
                        )
                    },

                    roleName = stringResource(Res.string.register_screen__role_organiser_title),
                    roleDescription = stringResource(Res.string.register_screen__role_organiser_text),
                )

            }
            Spacer(Modifier.height(64.dp))
            ButtonPrimary(
                ButtonColorType.ORANGE, onClick = {
                    component.onEvent(RegisterStep2ScreenEvent.OnNextStepButtonClick)
                }, text = stringResource(Res.string.next_step)
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AccountBox(
    onClick: () -> Unit, isSelected: Boolean, roleName: String, roleDescription: String
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Apple else LightGrey,
        animationSpec = tween(durationMillis = 300) // Customize the duration and easing if needed
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) OnApple else Color.Transparent,
        animationSpec = tween(durationMillis = 0)
    )

    Row(
        Modifier.fillMaxWidth().clip(Shapes.medium).background(backgroundColor)
            .border(1.dp, borderColor, Shapes.medium).clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
                role = Role.Button
            )
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    Modifier.width(48.dp).height(48.dp).clip(Shapes.medium)
                ) {
                    Box(Modifier.padding(12.dp)) {
                        Text(
                            "X",
                            Modifier.fillMaxWidth(),
                            style = Typography.h2,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(roleName, style = Typography.h3)
                    Text(
                        roleDescription,
                        fontSize = Typography.body1.fontSize,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}