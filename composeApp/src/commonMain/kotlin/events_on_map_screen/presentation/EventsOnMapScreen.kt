package events_on_map_screen.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.domain.EventMarker
import core.domain.GpsPosition
import core.presentation.components.map_view.LocationVisualizer
import events_on_map_screen.presentation.component.EventsOnMapScreenComponent
import events_on_map_screen.presentation.component.EventsOnMapScreenEvent
import navigation.CustomBottomNavigation
import navigation.CustomTopBar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsOnMapScreen(component: EventsOnMapScreenComponent) {
    val verticalScrollEnableState = remember { mutableStateOf(true) }

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

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

        LocationVisualizer(
            Modifier.fillMaxSize().padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
            markers = component.markers.value,
            starterPosition = GpsPosition(48.946427, 18.118392),
            parentScrollEnableState = verticalScrollEnableState,
            onMarkerClick = {
                println("This is click from map event: $it")
                showBottomSheet = true
                true
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.height(400.dp),
                windowInsets = WindowInsets(0, 0, 0, -2),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                containerColor = MaterialTheme.colors.background,
                scrimColor = Color.Black.copy(alpha = 0.4f)

            ) {
                Column(Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp)) {
                    Text(
                        "Name Surname",
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onBackground
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Email: 1234@mail.com",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onBackground
                        )

                        Text(
                            "Phone: 0915 123 123",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }
}

fun generateRandomPoints(
    centerLat: Double,
    centerLon: Double,
    radiusInMeters: Double,
    numberOfPoints: Int
): List<EventMarker> {
    val points = mutableListOf<EventMarker>()
    val earthRadius = 6378137 // Earth radius in meters

    repeat(numberOfPoints) { index ->
        val randomAngle = Random.nextDouble(0.0, 2 * PI)
        val randomRadius = Random.nextDouble(0.0, radiusInMeters)

        val dx = randomRadius * cos(randomAngle)
        val dy = randomRadius * sin(randomAngle)

        val deltaLat = dy / earthRadius * (180 / PI)
        val deltaLon = dx / (earthRadius * cos(PI * centerLat / 180)) * (180 / PI)

        val pointLat = centerLat + deltaLat
        val pointLon = centerLon + deltaLon

        points.add(EventMarker("Event id $index", pointLat, pointLon))
    }

    return points
}