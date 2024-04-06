package event_detail.presentation.event_detail_worker

import SallaryObject
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.helpers.event.printifyEventDateTime
import core.data.helpers.event.printifyEventLocation
import core.domain.worker.AssignmentStatus
import core.presentation.components.event_card.EventStatusTag
import core.presentation.components.event_categories.EventCategories
import event_detail.data.dto.EventWorkerDto
import event_detail.data.dto.EventWorkersDto
import event_detail.data.dto.WorkerUserDto
import event_detail.presentation.event_detail_worker.component.EventDetailScreenComponent
import event_detail.presentation.event_detail_worker.component.EventDetailScreenEvent
import event_detail.presentation.event_detail_worker.layout_components.BottomBarWithActions
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.categories
import grabit.composeapp.generated.resources.event_detail_screen__signed_at
import grabit.composeapp.generated.resources.event_detail_screen__signed_for_workers
import grabit.composeapp.generated.resources.event_detail_screen__signed_out_at
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import printifySallary
import ui.theme.LightOnOrange
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(component: EventDetailScreenComponent) {
    val stateEventDetail by component.stateEventDetail.subscribeAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var showBottomSheet by remember { mutableStateOf(false) }
    var workerDetailData by remember { mutableStateOf<EventWorkerDto?>(null) }

    val sheetState = rememberModalBottomSheetState()
    rememberCoroutineScope()

    LaunchedEffect(true) {
        component.loadEventData()
    }

    if (!stateEventDetail.isLoadingEventData) {
        Scaffold(topBar = {
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
        }, bottomBar = {
            if (!stateEventDetail.isLoadingEventData) {
                BottomBarWithActions(stateEventDetail.userPermissions!!, component)
            }
        }) { paddingValues ->
            Box(
                Modifier.fillMaxHeight().fillMaxWidth().verticalScroll(rememberScrollState())
                    .padding(bottom = paddingValues.calculateBottomPadding() + 24.dp).background(
                        MaterialTheme.colors.background
                    )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Box {
                        AsyncImage(
                            modifier = Modifier.height(196.dp).clip(Shapes.medium),
                            model = stateEventDetail.eventDetail!!.thumbnailURL,
                            contentDescription = null,
                            imageLoader = ImageLoader(LocalPlatformContext.current),
                            contentScale = ContentScale.Crop,
                        )

                        Box(Modifier.align(Alignment.TopEnd).padding(end = 8.dp, top = 8.dp)) {
                            EventStatusTag(stateEventDetail.eventDetail!!.status)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stateEventDetail.eventDetail!!.name,
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stateEventDetail.eventDetail!!.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondary
                    )

                    Spacer(Modifier.height(40.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                        InfoRow(
                            stringResource(Res.string.capacity),
                            Res.drawable.profile,
                            "Free places: ${stateEventDetail.eventDetail!!.capacity - stateEventDetail.eventDetail!!.count.eventAssignment}"
                        )

                        InfoRow(
                            stringResource(Res.string.organizer),
                            Res.drawable.home,
                            stateEventDetail.eventDetail!!.user.name
                        )

                        InfoRow(
                            stringResource(Res.string.starts_at),
                            Res.drawable.time_circle,
                            printifyEventDateTime(stateEventDetail.eventDetail!!.happeningAt)
                        )

                        if (stateEventDetail.eventDetail!!.toolingRequired !== null || stateEventDetail.eventDetail!!.toolingProvided !== null) {
                            Column {
                                Text(
                                    text = stringResource(Res.string.tooling),
                                    style = MaterialTheme.typography.h2,
                                    color = MaterialTheme.colors.onBackground
                                )
                                Spacer(Modifier.height(4.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (stateEventDetail.eventDetail!!.toolingProvided !== null) {
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
                                                    text = stateEventDetail.eventDetail!!.toolingProvided.toString(),
                                                    style = MaterialTheme.typography.body1,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                        }
                                    }

                                    if (stateEventDetail.eventDetail!!.toolingRequired !== null) {
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
                                                    text = stateEventDetail.eventDetail!!.toolingRequired.toString(),
                                                    style = MaterialTheme.typography.body1,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        InfoRow(
                            stringResource(Res.string.location),
                            Res.drawable.location,
                            text = "${stateEventDetail.eventDetail!!.location.name}\n${stateEventDetail.eventDetail!!.location.address}\n${stateEventDetail.eventDetail!!.location.city}"
                        )

                        InfoRow(
                            stringResource(Res.string.salary),
                            Res.drawable.sallary,
                            text = printifySallary(
                                SallaryObject(
                                    stateEventDetail.eventDetail!!.sallaryType,
                                    stateEventDetail.eventDetail!!.sallaryAmount,
                                    stateEventDetail.eventDetail!!.sallaryProductName,
                                    stateEventDetail.eventDetail!!.sallaryUnit,
                                )
                            )
                        )

                        Column {
                            Text(
                                text = stringResource(Res.string.categories),
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )
                            Spacer(Modifier.height(4.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                EventCategories(stateEventDetail.eventDetail!!.eventCategoryRelation.map { categoryRelation ->
                                    categoryRelation.eventCategory
                                })
                            }
                        }

                        if (stateEventDetail.userPermissions!!.displayOrganiserControls) {
                            if (stateEventDetail.isLoadingWorkersData) {
                                CircularProgressIndicator()
                            } else {
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
                                            text = "${stateEventDetail.eventDetail!!.count.eventAssignment} / ${stateEventDetail.eventDetail!!.capacity}",
                                            style = MaterialTheme.typography.body1,
                                            color = MaterialTheme.colors.onBackground
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),

                                        ) {
                                        stateEventDetail.eventWorkers!!.workers.forEach { worker ->
                                            WorkerBox(
                                                worker = worker,
                                                onClick = { it ->
                                                    workerDetailData = it
                                                    showBottomSheet = true
                                                }
                                            )

                                        }
                                    }

                                }
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
                                workerDetailData!!.user.name,
                                style = MaterialTheme.typography.h1,
                                color = MaterialTheme.colors.onBackground
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    "Email: ${workerDetailData!!.user.email}",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )

                                Text(
                                    "Phone: ${workerDetailData!!.user.phoneNumber}",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                                Text(

                                    "${stringResource(Res.string.event_detail_screen__signed_at)} ${
                                        printifyEventDateTime(
                                            workerDetailData!!.createdAt
                                        )
                                    }",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    } else if (stateEventDetail.isLoadingEventData) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colors.secondary, strokeWidth = 3.dp)
        }
    } else if (stateEventDetail.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stateEventDetail.error.toString(),
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            )
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
            Modifier, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Top
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WorkerBox(
    worker: EventWorkerDto,
    onClick: (worker: EventWorkerDto) -> Unit
) {

    val signedText =
        if (worker.assignmentStatus == AssignmentStatus.ACTIVE)
            stringResource(Res.string.event_detail_screen__signed_at)
        else
            stringResource(Res.string.event_detail_screen__signed_out_at)

    Row(
        Modifier.fillMaxWidth().background(
            MaterialTheme.colors.surface, Shapes.medium
        ).padding(
            start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = worker.user.name,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )

            Text(
                text = signedText + " " + printifyEventDateTime(worker.createdAt),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )
        }
        IconButton(
            modifier = Modifier.padding(2.dp).background(
                MaterialTheme.colors.surface, Shapes.medium
            ),
            onClick = { onClick(worker) }
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = vectorResource(Res.drawable.eye),
                contentDescription = null
            )
        }
    }
}
