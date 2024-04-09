package home_screen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.remote.dto.EventCardDto
import core.presentation.components.event_card.EventCard
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.home_screen__newest_harvests_title
import grabit.composeapp.generated.resources.home_screen__welcome_message_text
import grabit.composeapp.generated.resources.home_screen__welcome_message_title
import home_screen.presentation.component.HomeScreenComponent
import home_screen.presentation.component.HomeScreenEvent
import navigation.CustomBottomNavigation
import navigation.CustomTopBar
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.WelcomeGreen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(component: HomeScreenComponent) {
    val isLoading by component.isPopularEventsLoading.subscribeAsState()
    val latestEvents by component.latestEvents.subscribeAsState()

    val location =
        rememberLocationTrackerFactory(accuracy = LocationTrackerAccuracy.Medium).createLocationTracker()

    LaunchedEffect(true) {
        component.loadLatestEvents()

        location
            .startTracking()
            location.getLocationsFlow()
            .collect {
                println("LocationGPS: " + it)
            }
    }

    Scaffold(
        topBar = {
            CustomTopBar {
                component.onEvent(HomeScreenEvent.NavigateToAccountDetailScreen)
            }
        },
        bottomBar = {
            CustomBottomNavigation(0) {
                component.onEvent(HomeScreenEvent.NavigateBottomBarItem(it))
            }
        },
    ) { paddingValues ->
        Column(
            Modifier.background(MaterialTheme.colors.background)
                .verticalScroll(state = rememberScrollState()).padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = paddingValues.calculateBottomPadding() + 24.dp,
                    top = 64.dp
                )

        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(Res.string.home_screen__welcome_message_title),
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WelcomeGreen,
                        fontFamily = MaterialTheme.typography.h1.fontFamily
                    )
                )

                Text(
                    text = stringResource(Res.string.home_screen__welcome_message_text),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary
                )
            }
            Spacer(Modifier.height(42.dp))
            Column(verticalArrangement = Arrangement.spacedBy(42.dp)) {
                EventsSlider(
                    component,
                    latestEvents,
                    stringResource(Res.string.home_screen__newest_harvests_title)
                )
//                EventsSlider(images, stringResource(Res.string.home_screen__harvests_nearby_title))
            }
        }
    }
}

@Composable
fun EventsSlider(component: HomeScreenComponent, events: List<EventCardDto>, sliderTitle: String) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            sliderTitle,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )
        // Scrollable Row of Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            events.forEachIndexed { _, event ->
                EventCard(
                    event = event, onClick = {
                        component.onEvent(
                            HomeScreenEvent.NavigateToEventDetailScreen(it)
                        )
                    },
                    modifier = Modifier.width(280.dp).fillMaxHeight()
                )
            }
        }
    }
}