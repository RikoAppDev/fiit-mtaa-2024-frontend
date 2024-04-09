package events_on_map_screen.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.helpers.event.printifyEventDateTime
import core.data.helpers.event.printifyEventLocation
import core.domain.EventMarker
import core.domain.GpsPosition
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.event_categories.EventCategories
import core.presentation.components.map_view.LocationVisualizer
import events_on_map_screen.presentation.component.EventsOnMapScreenComponent
import events_on_map_screen.presentation.component.EventsOnMapScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.events_on_map_screen__show_detail
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.time_circle
import navigation.CustomBottomNavigation
import navigation.CustomTopBar
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.domain.ColorVariation
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun EventsOnMapScreen(component: EventsOnMapScreenComponent) {
    val verticalScrollEnableState = remember { mutableStateOf(true) }

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val eventsOnMapState by component.eventsOnMapState.subscribeAsState()

    LaunchedEffect(true) {
        component.getPoints()
    }

    Scaffold(
        topBar = {
            CustomTopBar {
                component.onEvent(EventsOnMapScreenEvent.NavigateToAccountDetailScreen)
            }
        },
        bottomBar = {
            CustomBottomNavigation(2) {
                component.onEvent(EventsOnMapScreenEvent.NavigateBottomBarItem(it))
            }
        },

        ) { paddingValues ->

        if (!eventsOnMapState.isLoadingPoints) {
            LocationVisualizer(
                Modifier.fillMaxSize().padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
                markers = eventsOnMapState.points!!,
                starterPosition = GpsPosition(48.946427, 18.118392),
                parentScrollEnableState = verticalScrollEnableState,
                onMarkerClick = {
                    component.onEvent(EventsOnMapScreenEvent.OnEventOnMapClick(it))
                    showBottomSheet = true
                    true
                }
            )
        } else {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.height(400.dp),
                windowInsets = WindowInsets(0, 0, 0, -2),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                containerColor = MaterialTheme.colors.background,
                scrimColor = Color.Black.copy(alpha = 0.4f)

            ) {
                if (!eventsOnMapState.isLoadingEventSneakPeak) {
                    Column(
                        Modifier.fillMaxWidth().navigationBarsPadding()
                            .padding(start = 24.dp, end = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text(
                                eventsOnMapState.eventSneakPeak!!.name,
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = vectorResource(Res.drawable.location),
                                        contentDescription = stringResource(Res.string.logo),
                                        tint = MaterialTheme.colors.secondary
                                    )
                                    Text(
                                        printifyEventLocation(eventsOnMapState.eventSneakPeak!!.location),
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = vectorResource(Res.drawable.time_circle),
                                        contentDescription = stringResource(Res.string.logo),
                                        tint = MaterialTheme.colors.secondary
                                    )
                                    Text(
                                        printifyEventDateTime(eventsOnMapState.eventSneakPeak!!.happeningAt),
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }
                        }

                        Column(
                            Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            EventCategories(
                                eventsOnMapState.eventSneakPeak!!.eventCategoryRelation.map {
                                    it.eventCategory
                                }
                            )

                            ButtonPrimary(ColorVariation.APPLE,
                                text = stringResource(Res.string.events_on_map_screen__show_detail),
                                onClick = {
                                    component.onEvent(
                                        EventsOnMapScreenEvent.NavigateToEventDetail(
                                            eventsOnMapState.eventSneakPeak!!.id
                                        )
                                    )
                                }
                            )
                        }
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
