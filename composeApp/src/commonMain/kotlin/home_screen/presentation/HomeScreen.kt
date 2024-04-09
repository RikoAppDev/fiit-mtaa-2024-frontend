package home_screen.presentation

import account_detail.presentation.account_detail.component.AccountDetailScreenEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.login.component.LoginScreenEvent
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.remote.dto.EventCardDto
import core.presentation.components.cicrular_progress.CustomCircularProgress
import core.presentation.components.event_card.EventCard
import core.presentation.components.snackbar.CustomSnackbar
import core.presentation.components.snackbar.SnackbarVisualWithError
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.home_screen__newest_harvests_title
import grabit.composeapp.generated.resources.home_screen__welcome_message_text
import grabit.composeapp.generated.resources.home_screen__welcome_message_title
import home_screen.presentation.component.HomeScreenComponent
import home_screen.presentation.component.HomeScreenEvent
import kotlinx.coroutines.launch
import navigation.CustomBottomNavigation
import navigation.CustomTopBar
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.LightOnOrange
import ui.theme.Shapes
import ui.theme.WelcomeGreen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(component: HomeScreenComponent) {
    val homescreenState by component.homeScreenState.subscribeAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isVisible = remember { mutableStateOf(false) }

    val location =
        rememberLocationTrackerFactory(accuracy = LocationTrackerAccuracy.Medium).createLocationTracker()

    LaunchedEffect(true) {
        component.loadLatestEvents()
        component.getLatestEvent()

        location
            .startTracking()
        location.getLocationsFlow()
            .collect {
                println("LocationGPS: " + it)
            }
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

            if (!homescreenState.isActiveEventLoading && homescreenState.activeEvent != null) {
                Spacer(Modifier.height(24.dp))
                Row(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth().clickable {
                            component.onEvent(HomeScreenEvent.NavigateToActiveEvent(homescreenState.activeEvent!!.id))
                        }
                        .background(MaterialTheme.colors.surface)
                        .padding(12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column {
                                AsyncImage(
                                    modifier = Modifier.size(48.dp).clip(Shapes.medium),
                                    model = homescreenState.activeEvent!!.thumbnailURL,
                                    contentDescription = null,
                                    imageLoader = ImageLoader(LocalPlatformContext.current),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            Column {
                                Text(
                                    homescreenState.activeEvent!!.name,
                                    style = MaterialTheme.typography.h3,
                                    color = MaterialTheme.colors.onBackground
                                )
                                Text(
                                    homescreenState.activeEvent!!.user.name,
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "",
                            tint = LightOnOrange
                        )
                    }
                }
            }

            if (!homescreenState.isLatestEventsLoading) {
                Spacer(Modifier.height(42.dp))
                Column(verticalArrangement = Arrangement.spacedBy(42.dp)) {
                    EventsSlider(
                        component,
                        homescreenState.latestEvents!!.events,
                        stringResource(Res.string.home_screen__newest_harvests_title),
                        homescreenState.isLatestEventsLoading
                    )
//                EventsSlider(images, stringResource(Res.string.home_screen__harvests_nearby_title))
                }
            }


        }
    }

    if (!isVisible.value && homescreenState.error != "") {
        coroutineScope.launch {
            isVisible.value = true
            val snackbarResult = snackbarHostState.showSnackbar(
                message = homescreenState.error,
                duration = SnackbarDuration.Short
            )

            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    isVisible.value = false
                    component.onEvent(HomeScreenEvent.RemoveError)
                }

                SnackbarResult.ActionPerformed -> {
                    isVisible.value = false
                    component.onEvent(HomeScreenEvent.RemoveError)
                }
            }
        }
    }
}

@Composable
fun EventsSlider(
    component: HomeScreenComponent,
    events: List<EventCardDto>,
    sliderTitle: String,
    isLoading: Boolean = false
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CustomCircularProgress(size = 40.dp)
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
}