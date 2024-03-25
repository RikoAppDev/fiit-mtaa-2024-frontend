package home_screen.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import core.presentation.components.category_chip.CategoryChip
import core.presentation.components.event_card.EventCard
import event_detail.presentation.event_create.component.EventCreateScreenComponent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.grabit
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.profile
import grabit.composeapp.generated.resources.time_circle
import home_screen.presentation.component.HomeScreenComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.domain.ColorVariation
import ui.theme.LightGrey
import ui.theme.Lime
import ui.theme.MenuActive
import ui.theme.OnLime
import ui.theme.SecondaryText
import ui.theme.Shapes
import ui.theme.Typography
import ui.theme.WelcomeGreen

@OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(component: HomeScreenComponent) {
    val images = listOf(
        "https://media.npr.org/assets/img/2021/08/11/gettyimages-1279899488_wide-f3860ceb0ef19643c335cb34df3fa1de166e2761-s1100-c50.jpg",
        "https://cdn.pixabay.com/photo/2017/02/20/18/03/cat-2083492__480.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRrfPnodZbEjtJgE-67C-0W9pPXK8G9Ai6_Rw&usqp=CAU",
        "https://i.ytimg.com/vi/E9iP8jdtYZ0/maxresdefault.jpg",
        "https://cdn.shopify.com/s/files/1/0535/2738/0144/articles/shutterstock_149121098_360x.jpg"
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.shadow(
                    elevation = 16.dp,
                    spotColor = Color(0x40CACACA),
                    ambientColor = Color(0x40CACACA)
                ),
                contentColor = Color.Black,
                backgroundColor = Color.White,
            ) {
                Row(Modifier.padding(bottom = 32.dp, start = 24.dp, end = 24.dp, top = 16.dp)) {
                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = true,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})
                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = false,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})
                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    },
                        label = { Text("Nav 1") },
                        selected = false,
                        selectedContentColor = MenuActive,
                        unselectedContentColor = SecondaryText,
                        onClick = {})

                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null
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
        topBar = {
            TopAppBar(
                modifier = Modifier.height(148.dp).shadow(
                    elevation = 16.dp,
                    spotColor = Color(0x40E9E9E9),
                    ambientColor = Color(0x40E9E9E9)
                ), backgroundColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().safeDrawingPadding()
                        .padding(start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(Modifier.width(120.dp)) {
                        Image(
                            imageVector = vectorResource(Res.drawable.grabit),
                            contentDescription = stringResource(Res.string.logo),
                        )
                    }
                    Box(Modifier.clip(CircleShape).background(LightGrey).padding(6.dp)) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            imageVector = vectorResource(Res.drawable.profile),
                            contentDescription = stringResource(Res.string.logo),
                        )
                    }
                }
            }
        },

        ) {
        Column(
            Modifier.background(Color.White).verticalScroll(state = rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 128.dp, top = 64.dp)

        ) {

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 40.sp, fontWeight = FontWeight.SemiBold, color = Color.Black
                        )
                    ) {
                        append("Vitajte, ")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 40.sp, fontWeight = FontWeight.SemiBold, color = WelcomeGreen
                        )
                    ) {
                        append("Martin!")
                    }
                })
                Text(
                    "Pozrite sa čo je nové vo svete zberačov",
                    style = Typography.body1,
                    color = SecondaryText
                )
            }
            Spacer(Modifier.height(42.dp))
            Column(verticalArrangement = Arrangement.spacedBy(42.dp)) {
                EventsSlider(images, "Najnovšie udalosti")
                EventsSlider(images, "Udalosti v okolí")
            }

        }
    }
//    Surface(
//        Modifier.background(Color.White).fillMaxWidth()
//            .zIndex(1f)
//            .clip(GenericShape { size, _ ->
//                lineTo(size.width, 0f)
//                lineTo(size.width, Float.MAX_VALUE)
//                lineTo(0f, Float.MAX_VALUE)
//            })
//            .shadow(
//                elevation = 24.dp,
//                spotColor = Color(0x40E9E9E9),
//            )
//
//    ) {
//        Row(
//            Modifier.fillMaxWidth().safeContentPadding().padding(
//                start = 24.dp,
//                end = 24.dp,
//                top = 24.dp,
//                bottom = 24.dp
//            ),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Box(Modifier.width(120.dp)) {
//                Image(
//                    imageVector = vectorResource(Res.drawable.grabit),
//                    contentDescription = stringResource(Res.string.logo),
//                )
//            }
//            Box(Modifier.clip(CircleShape).background(LightGrey).padding(6.dp)) {
//                Image(
//                    modifier = Modifier.size(20.dp),
//                    imageVector = vectorResource(Res.drawable.profile),
//                    contentDescription = stringResource(Res.string.logo),
//                )
//            }
//        }
//    }

}

@Composable
fun EventsSlider(images: List<String>, sliderTitle: String) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(sliderTitle, style = Typography.h2)
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