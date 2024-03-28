package event_detail.presentation.event_detail_worker

import account_detail.presentation.account_detail.component.AccountDetailScreenEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.category_chip.CategoryChip
import core.presentation.components.filled_input.FilledInput
import event_detail.presentation.event_detail_worker.component.EventDetailScreenComponent
import event_detail.presentation.event_detail_worker.component.EventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.account_detail__discard_changed
import grabit.composeapp.generated.resources.account_detail__save_changes
import grabit.composeapp.generated.resources.account_detail__screen_title
import grabit.composeapp.generated.resources.account_detail__update
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.categories
import grabit.composeapp.generated.resources.event_detail_screen__featured_image
import grabit.composeapp.generated.resources.event_detail_screen__title
import grabit.composeapp.generated.resources.home
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.organizer
import grabit.composeapp.generated.resources.phone_number
import grabit.composeapp.generated.resources.placeholder
import grabit.composeapp.generated.resources.placeholder_icon
import grabit.composeapp.generated.resources.profile
import grabit.composeapp.generated.resources.salary
import grabit.composeapp.generated.resources.sallary
import grabit.composeapp.generated.resources.starts_at
import grabit.composeapp.generated.resources.time_circle
import grabit.composeapp.generated.resources.tooling
import grabit.composeapp.generated.resources.tooling_provided
import grabit.composeapp.generated.resources.tooling_required
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.domain.ColorVariation
import ui.theme.LightApple
import ui.theme.LightGrey
import ui.theme.LightOnApple
import ui.theme.LightOnOrange
import ui.theme.SecondaryText
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EventDetailScreen(component: EventDetailScreenComponent) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.surface,
                    titleContentColor = MaterialTheme.colors.onBackground,
                ),

                title = {
                    Text(
                        stringResource(Res.string.event_detail_screen__title),
                        style = MaterialTheme.typography.h3,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = LightOnOrange
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {

            BottomNavigation(
                elevation = 16.dp,
            ) {
                Box(
                    Modifier.background(MaterialTheme.colors.background).navigationBarsPadding().padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 24.dp,
                    ),
                    Alignment.BottomCenter
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        ButtonPrimary(
                            type = ColorVariation.APPLE,
                            text = "Prihlásiť sa na zber",
                            onClick = {}
                        )
                    }
                }
            }
        }

    ) {paddingValues ->
        Box(
            Modifier.fillMaxHeight().verticalScroll(rememberScrollState()).padding(bottom = paddingValues.calculateBottomPadding() + 24.dp)
                .background(
                    MaterialTheme.colors.background
                )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                AsyncImage(
                    modifier = Modifier.height(196.dp).clip(Shapes.medium),
                    model = "https://picsum.photos/seed/picsum/1280/720",
                    contentDescription = null,
                    imageLoader = ImageLoader(LocalPlatformContext.current),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Názov zberu",
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onBackground

                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Lorem ipsum dolor  sit amet, consectetur adipiscing elit. Quisque blandit convallis eros in lobortis. Praesent sagittis sem non felis",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary
                )

                Spacer(Modifier.height(40.dp))

                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    InfoRow(
                        stringResource(Res.string.capacity),
                        Res.drawable.profile,
                        "Free places: 4"
                    )

                    InfoRow(
                        stringResource(Res.string.organizer),
                        Res.drawable.home,
                        "Družstvo Nemšová"
                    )

                    InfoRow(
                        stringResource(Res.string.starts_at),
                        Res.drawable.time_circle,
                        "21.06.2002 - 21:50"
                    )

                    Column {
                        Text(
                            text = stringResource(Res.string.tooling),
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.secondary
                        )
                        Spacer(Modifier.height(4.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                Modifier.fillMaxWidth().clip(Shapes.medium).background(MaterialTheme.colors.surface),

                                ) {
                                Column(
                                    Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(Res.string.tooling_provided),
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                    Text(
                                        text = "Rukavice , kýble, hrable",
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }

                            Box(
                                Modifier.fillMaxWidth().clip(Shapes.medium).background(MaterialTheme.colors.surface),

                                ) {
                                Column(
                                    Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(Res.string.tooling_required),
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                    Text(
                                        text = "Monterky, kalíšok",
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }
                        }
                    }

                    InfoRow(
                        stringResource(Res.string.location),
                        Res.drawable.location,
                        "Družstvo Vyšné Žrďky" +
                                "\nPalackého 24/11" +
                                "\nNemšová"
                    )

                    InfoRow(
                        stringResource(Res.string.salary),
                        Res.drawable.sallary,
                        "Zemiaky - 0.5kg / h"
                    )

                    Column {
                        Text(
                            text = stringResource(Res.string.categories),
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onBackground
                        )
                        Spacer(Modifier.height(8.dp))

                        Row(
                            Modifier,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CategoryChip("🥔 Zemiaky")
                            CategoryChip("🍠 Reťkovka", color = ColorVariation.ORANGE)
                        }
                    }
                }
            }
        }


    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun InfoRow(title: String, icon: DrawableResource, text: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(Modifier.height(4.dp))

        Row(
            Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = vectorResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}
