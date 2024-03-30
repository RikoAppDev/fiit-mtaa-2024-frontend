package event_detail.presentation.event_detail_worker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.category_chip.CategoryChip
import event_detail.presentation.event_detail_worker.component.EventDetailScreenComponent
import event_detail.presentation.event_detail_worker.component.EventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.categories
import grabit.composeapp.generated.resources.event_detail_screen__edit
import grabit.composeapp.generated.resources.event_detail_screen__end_harvest
import grabit.composeapp.generated.resources.event_detail_screen__end_harvest_notice
import grabit.composeapp.generated.resources.event_detail_screen__sign_in_for_harvest
import grabit.composeapp.generated.resources.event_detail_screen__signed_for_workers
import grabit.composeapp.generated.resources.event_detail_screen__start_event
import grabit.composeapp.generated.resources.event_detail_screen__title
import grabit.composeapp.generated.resources.eye
import grabit.composeapp.generated.resources.home
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.organizer
import grabit.composeapp.generated.resources.profile
import grabit.composeapp.generated.resources.salary
import grabit.composeapp.generated.resources.sallary
import grabit.composeapp.generated.resources.starts_at
import grabit.composeapp.generated.resources.time_circle
import grabit.composeapp.generated.resources.tooling
import grabit.composeapp.generated.resources.tooling_provided
import grabit.composeapp.generated.resources.tooling_required
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.domain.ColorVariation
import ui.theme.LightOnOrange
import ui.theme.Shapes

@OptIn(
    ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Preview
@Composable
fun EventDetailScreen(component: EventDetailScreenComponent) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isLoading by component.isLoading.subscribeAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()


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
                        component.onEvent(EventDetailScreenEvent.NavigateBack)
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
            if (isLoading) return@Scaffold
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
                        ),
                    Alignment.BottomCenter
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        ButtonPrimary(
                            type = ColorVariation.APPLE,
                            text = stringResource(Res.string.event_detail_screen__sign_in_for_harvest),
                            onClick = {}
                        )
                    }

                }
            }
        }

    ) { paddingValues ->

        Box(
            Modifier.fillMaxHeight().fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(bottom = paddingValues.calculateBottomPadding() + 24.dp)
                .background(
                    MaterialTheme.colors.background
                )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    Modifier.align(Alignment.Center),

                    color = MaterialTheme.colors.secondary,
                    strokeWidth = 3.dp,
                )
            } else {
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
                                    Modifier.fillMaxWidth().clip(Shapes.medium)
                                        .background(MaterialTheme.colors.surface),

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
                                    Modifier.fillMaxWidth().clip(Shapes.medium)
                                        .background(MaterialTheme.colors.surface),

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

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(Res.string.event_detail_screen__signed_for_workers),
                                    style = MaterialTheme.typography.h2,
                                    color = MaterialTheme.colors.onBackground
                                )

                                Text(
                                    text = "4/10",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),

                                ) {
                                listOf<String>("1", "2", "3", "4").forEach {
                                    Row(
                                        Modifier.fillMaxWidth().background(
                                            MaterialTheme.colors.surface,
                                            Shapes.medium
                                        ).padding(
                                            start = 12.dp,
                                            end = 12.dp,
                                            top = 8.dp,
                                            bottom = 8.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Name Surname",
                                                style = MaterialTheme.typography.h3,
                                                color = MaterialTheme.colors.onBackground
                                            )

                                            Text(
                                                text = "Signed at 12.4.2024 15:00",
                                                style = MaterialTheme.typography.body2,
                                                color = MaterialTheme.colors.secondary
                                            )
                                        }
                                        IconButton(
                                            modifier = Modifier.padding(2.dp)
                                                .background(
                                                    MaterialTheme.colors.surface,
                                                    Shapes.medium
                                                ),
                                            onClick = {
                                                showBottomSheet = true
                                            },
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(20.dp),
                                                imageVector = vectorResource(Res.drawable.eye),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }


                    //DEV Button layouts
                    Column(Modifier.padding(top = 48.dp)) {
                        Box(Modifier.fillMaxWidth()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ButtonPrimary(
                                    buttonModifier = Modifier.weight(1f),
                                    type = ColorVariation.ORANGE,
                                    text = stringResource(Res.string.event_detail_screen__edit),
                                    onClick = {}
                                )
                                ButtonPrimary(
                                    buttonModifier = Modifier.weight(1f),
                                    type = ColorVariation.APPLE,
                                    text = stringResource(Res.string.event_detail_screen__start_event),
                                    onClick = {}
                                )
                            }
                        }

                        Box(Modifier.fillMaxWidth()) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ButtonPrimary(
                                    type = ColorVariation.CHERRY,
                                    text = stringResource(Res.string.event_detail_screen__end_harvest),
                                    onClick = {}
                                )
                                Text(
                                    text = stringResource(Res.string.event_detail_screen__end_harvest_notice),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                        Box(Modifier.fillMaxWidth()) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "You cant sign as organiser",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
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
