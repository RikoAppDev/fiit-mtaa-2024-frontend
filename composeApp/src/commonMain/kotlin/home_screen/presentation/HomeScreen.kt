package home_screen.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.components.event_card.EventCard
import core.presentation.components.themed_logo.ThemedLogo
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.home_screen__harvests_nearby_title
import grabit.composeapp.generated.resources.home_screen__newest_harvests_title
import grabit.composeapp.generated.resources.home_screen__welcome_message_text
import grabit.composeapp.generated.resources.home_screen__welcome_message_title
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.profile
import home_screen.presentation.component.HomeScreenComponent
import home_screen.presentation.component.HomeScreenEvent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.theme.LightGrey
import ui.theme.MenuActive
import ui.theme.SecondaryText
import ui.theme.WelcomeGreen

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(component: HomeScreenComponent) {
    val images = listOf(
        "https://picsum.photos/seed/grabiT1/1280/720",
        "https://picsum.photos/seed/grabiT2/1280/720",
        "https://picsum.photos/seed/grabiT3/1280/720",
        "https://picsum.photos/seed/grabiT4/1280/720",
        "https://picsum.photos/seed/grabiT5/1280/720"
    )

    val topBarModifier = if (isSystemInDarkTheme()) {
        Modifier.background(MaterialTheme.colors.background).displayCutoutPadding().height(80.dp)
    } else {
        Modifier.background(Color.White).displayCutoutPadding().height(80.dp)
            .shadow(
                elevation = 16.dp,
                spotColor = Color(0x40E9E9E9),
                ambientColor = Color(0x40E9E9E9)
            )
    }

    val bottomBarModifier = if (isSystemInDarkTheme()) {
        Modifier
    } else {
        Modifier.shadow(
            elevation = 16.dp,
            spotColor = Color(0x40CACACA),
            ambientColor = Color(0x40CACACA)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(Modifier.width(120.dp)) {
                        ThemedLogo()
                    }
                    Box(Modifier.clip(CircleShape).clickable(
                        onClick = {
                            component.onEvent(HomeScreenEvent.NavigateToAccountDetailScreen)
                        }
                    )

                        .background(MaterialTheme.colors.surface)
                        .padding(6.dp)
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
                        .padding(bottom = 16.dp, start = 24.dp, end = 24.dp, top = 16.dp)
                ) {
                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = true,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})
                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = false,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})
                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = false,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})

                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = false,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})
                }
            }
        },

        ) { paddingValues ->
        Column(
            Modifier.background(MaterialTheme.colors.background)
                .verticalScroll(state = rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = paddingValues.calculateBottomPadding() + 24.dp, top = 64.dp)

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
                EventsSlider(images, stringResource(Res.string.home_screen__newest_harvests_title))
                EventsSlider(images, stringResource(Res.string.home_screen__harvests_nearby_title))
            }
        }
    }
}

@Composable
fun EventsSlider(images: List<String>, sliderTitle: String) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            sliderTitle,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Scrollable Row of Cards
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(images) { index, image ->
                    Box(Modifier.width(280.dp)) {
                        EventCard(image)
                    }
                }
            }
        }
    }
}