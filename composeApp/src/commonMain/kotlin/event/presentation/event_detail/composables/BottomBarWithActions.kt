package event.presentation.event_detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import event.domain.UserPermissions
import event.presentation.event_detail.component.EventDetailScreenComponent
import event.presentation.event_detail.component.EventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_detail_screen__cannot_register_as_organiser
import grabit.composeapp.generated.resources.event_detail_screen__capacity_full
import grabit.composeapp.generated.resources.event_detail_screen__edit
import grabit.composeapp.generated.resources.event_detail_screen__sign_in_for_harvest
import grabit.composeapp.generated.resources.event_detail_screen__sign_off_harvest
import grabit.composeapp.generated.resources.event_detail_screen__start_event
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.domain.ColorVariation

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomBarWithActions(permissions: UserPermissions, component: EventDetailScreenComponent) {
    val stateEventDetail by component.stateEventDetail.subscribeAsState()
    BottomNavigation(
        elevation = 16.dp,
    ) {
        Box(
            Modifier.background(MaterialTheme.colors.background).navigationBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 24.dp,
                ), Alignment.BottomCenter
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (permissions.displaySignIn) {
                    ButtonPrimary(
                        type = ColorVariation.APPLE,
                        text = stringResource(Res.string.event_detail_screen__sign_in_for_harvest),
                        isLoading = stateEventDetail.isLoadingButton,
                        onClick = {
                            component.onEvent(EventDetailScreenEvent.SignInForEvent)
                        },
                    )
                }

                if (permissions.displaySignOff) {
                    ButtonPrimary(
                        type = ColorVariation.CHERRY,
                        text = stringResource(Res.string.event_detail_screen__sign_off_harvest),
                        isLoading = stateEventDetail.isLoadingButton,
                        onClick = {
                            component.onEvent(EventDetailScreenEvent.SignOffEvent)
                        },
                    )
                }

                if (permissions.displayOrganiserControls) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ButtonPrimary(buttonModifier = Modifier.weight(1f),
                            type = ColorVariation.ORANGE,
                            text = stringResource(Res.string.event_detail_screen__edit),
                            onClick = {
                                component.onEvent(EventDetailScreenEvent.EditEvent)
                            })
                        ButtonPrimary(buttonModifier = Modifier.weight(1f),
                            type = ColorVariation.APPLE,
                            text = stringResource(Res.string.event_detail_screen__start_event),
                            onClick = {
                                component.onEvent(EventDetailScreenEvent.StartEvent)
                            })
                    }
                }

                if (permissions.displayCapacityFull) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.event_detail_screen__capacity_full),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center
                    )
                }

                if (permissions.displayCannotSignAsOrganizer) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.event_detail_screen__cannot_register_as_organiser),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }

}