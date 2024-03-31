package events_on_map_screen
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.data.getNavigationItems
import core.domain.EventMarker
import core.domain.GpsPosition
import core.presentation.components.map_view.LocationVisualizer
import core.presentation.components.logo.GrabItLogo
import events_on_map_screen.presentation.EventsOnMapScreenComponent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.theme.MenuActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventsOnMapScreen(component: EventsOnMapScreenComponent) {
    val verticalScrollEnableState = remember { mutableStateOf(true) }

    val topBarModifier = if (isSystemInDarkTheme()) {
        Modifier.background(MaterialTheme.colors.background).displayCutoutPadding().height(80.dp)
    } else {
        Modifier.background(Color.White).displayCutoutPadding().height(80.dp).shadow(
            elevation = 16.dp, spotColor = Color(0x40E9E9E9), ambientColor = Color(0x40E9E9E9)
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    var selected by mutableStateOf(0)

    val bottomBarModifier = if (isSystemInDarkTheme()) {
        Modifier
    } else {
        Modifier.shadow(
            elevation = 16.dp, spotColor = Color(0x40CACACA), ambientColor = Color(0x40CACACA)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = topBarModifier,
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        .padding(start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(Modifier.width(120.dp)) {
                        GrabItLogo()
                    }
                    Box(
                        Modifier.clip(CircleShape)

                            .background(MaterialTheme.colors.surface).padding(6.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = vectorResource(Res.drawable.profile),
                            contentDescription = stringResource(Res.string.logo),
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = bottomBarModifier,
                backgroundColor = MaterialTheme.colors.background,
            ) {
                Row(
                    Modifier.navigationBarsPadding()
                        .padding(bottom = 16.dp, start = 24.dp, end = 24.dp, top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    getNavigationItems().forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    item.icon, contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = TextStyle(
                                        fontFamily = MaterialTheme.typography.body2.fontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    )
                                )
                            },
                            selected = index == selected,
                            selectedContentColor = MenuActive,
                            unselectedContentColor = MaterialTheme.colors.secondary,
                            onClick = {
                                selected = index
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
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

fun generateRandomPoints(centerLat: Double, centerLon: Double, radiusInMeters: Double, numberOfPoints: Int): List<EventMarker> {
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