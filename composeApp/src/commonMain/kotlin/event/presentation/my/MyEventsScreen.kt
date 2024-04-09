package event.presentation.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import auth.domain.model.AccountType
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.cicrular_progress.CustomCircularProgress
import core.presentation.components.event_card.EventCard
import core.presentation.components.snackbar.CustomSnackbar
import core.presentation.components.snackbar.SnackbarVisualWithError
import event.presentation.my.component.MyEventsScreenComponent
import event.presentation.my.component.MyEventsScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.my_events_screen__no_events_harvester
import grabit.composeapp.generated.resources.my_events_screen__no_events_organiser
import grabit.composeapp.generated.resources.my_events_screen__subheading_harvester
import grabit.composeapp.generated.resources.my_events_screen__subheading_organiser
import grabit.composeapp.generated.resources.my_events_screen__title
import kotlinx.coroutines.launch
import navigation.CustomBottomNavigation
import navigation.CustomTopBar
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MyEventsScreen(component: MyEventsScreenComponent) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isVisible = remember { mutableStateOf(false) }
    val myEventsState by component.myEventsState.subscribeAsState()
    val accountType by component.accountType.subscribeAsState()


    val noEventsMessage =
        if (accountType == AccountType.HARVESTER.toString()) stringResource(Res.string.my_events_screen__no_events_organiser)
        else stringResource(Res.string.my_events_screen__no_events_harvester)

    val subheading =
        if (accountType == AccountType.HARVESTER.toString()) stringResource(Res.string.my_events_screen__subheading_harvester)
        else stringResource(Res.string.my_events_screen__subheading_organiser)


    LaunchedEffect(true) {
        component.loadMyEvents()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = {
                CustomSnackbar(
                    data = SnackbarVisualWithError(
                        snackbarData = it,
                        isError = true,
                    )
                )
            })
        },
        topBar = {
            CustomTopBar {
                component.onEvent(MyEventsScreenEvent.NavigateToAccountDetailScreen)
            }
        },
        bottomBar = {
            CustomBottomNavigation(3) {
                component.onEvent(MyEventsScreenEvent.NavigateBottomBarItem(it))
            }
        },
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(
                start = 24.dp, end = 24.dp, bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            if (!myEventsState.isLoadingEvents && myEventsState.events != null) {

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        stringResource(Res.string.my_events_screen__title),
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onBackground
                    )
                    if (myEventsState.events!!.events.isEmpty()) {
                        Text(
                            noEventsMessage,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onBackground
                        )
                    } else {
                        Text(
                            subheading,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground
                        )
                        Spacer(Modifier.height(32.dp))
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            itemsIndexed(myEventsState.events!!.events) { _, event ->
                                EventCard(
                                    event,
                                    {
                                        component.onEvent(
                                            MyEventsScreenEvent.NavigateToEventDetail(
                                                event.id
                                            )
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomCircularProgress(size = 40.dp)
                }
            }
        }

        if (!isVisible.value && myEventsState.errorEvents != null) {
            coroutineScope.launch {
                isVisible.value = true
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = myEventsState.errorEvents!!,
                    duration = SnackbarDuration.Short
                )

                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {
                        isVisible.value = false
                        component.onEvent(MyEventsScreenEvent.RemoveError)
                    }

                    SnackbarResult.ActionPerformed -> {
                        isVisible.value = false
                        component.onEvent(MyEventsScreenEvent.RemoveError)
                    }
                }
            }
        }
    }

}