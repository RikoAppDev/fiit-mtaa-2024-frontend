package event.presentation.event_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.helpers.event.printifyEventDateTime
import core.domain.worker.AssignmentStatus
import event.data.dto.EventWorkerDto
import event.presentation.composables.EventDetailsSection
import event.presentation.event_detail.component.EventDetailScreenComponent
import event.presentation.event_detail.component.EventDetailScreenEvent
import event.presentation.event_detail.composables.BottomBarWithActions
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_detail_screen__signed_at
import grabit.composeapp.generated.resources.event_detail_screen__signed_for_workers
import grabit.composeapp.generated.resources.event_detail_screen__signed_out_at
import grabit.composeapp.generated.resources.event_detail_screen__title
import grabit.composeapp.generated.resources.eye
import grabit.composeapp.generated.resources.top_bar_navigation__back
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ui.theme.LightOnOrange
import ui.theme.Shapes

@OptIn(
    ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun EventDetailScreen(component: EventDetailScreenComponent) {
    val stateEventDetail by component.stateEventDetail.subscribeAsState()

    val pullRefreshState = rememberPullRefreshState(stateEventDetail.isLoadingRefresh, {
        component.onEvent(EventDetailScreenEvent.Refresh)
    })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var showBottomSheet by remember { mutableStateOf(false) }
    var workerDetailData by remember { mutableStateOf<EventWorkerDto?>(null) }

    val sheetState = rememberModalBottomSheetState()
    rememberCoroutineScope()

    LaunchedEffect(true) {
        component.loadEventData()
    }

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
                        contentDescription = stringResource(Res.string.top_bar_navigation__back),
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
        if (!stateEventDetail.isLoadingEventData) {
            Box(
                Modifier.fillMaxSize().pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = paddingValues.calculateBottomPadding() + 24.dp).background(
                        MaterialTheme.colors.background
                    )
            ) {
                if (!stateEventDetail.isLoadingEventData) {
                    PullRefreshIndicator(
                        backgroundColor = MaterialTheme.colors.background,
                        contentColor = MaterialTheme.colors.onBackground,
                        refreshing = stateEventDetail.isLoadingRefresh,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter).zIndex(10f)
                    )
                }

                Column(Modifier.padding(24.dp)) {
                    if (stateEventDetail.eventDetail != null) {
                        EventDetailsSection(event = stateEventDetail.eventDetail!!)
                    }

                    if (stateEventDetail.userPermissions!!.displayOrganiserControls) {
                        if (stateEventDetail.isLoadingWorkersData) {
                            CircularProgressIndicator()
                        } else {
                            Column(
                                Modifier.padding(top = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
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
                                            onClick = {
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
        } else if (stateEventDetail.isLoadingEventData) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    strokeWidth = 3.dp
                )
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
