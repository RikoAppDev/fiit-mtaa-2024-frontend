package event.presentation.event_detail_live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import event.data.dto.AnnouncementItemDto
import event.data.dto.AttendanceWorkerDto
import event.domain.model.PresenceStatus
import event.presentation.event_detail_live.component.InProgressEventDetailScreenComponent
import event.presentation.event_detail_live.component.InProgressEventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.in_progress_event_detail_screen__title
import grabit.composeapp.generated.resources.organiser
import grabit.composeapp.generated.resources.present
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import printifyPresence
import ui.data.getButtonColors
import ui.domain.ColorVariation
import ui.theme.LightOnOrange
import ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun InProgressEventDetailScreen(component: InProgressEventDetailScreenComponent) {
    val announcementItemsScrollState = rememberLazyListState()
    val harmonogramItemsScrollState = rememberLazyListState()

    val inProgressEventDetailState by component.inProgressEventDetailState.subscribeAsState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(key1 = true) {
        component.loadInProgressEventData()
    }

    LaunchedEffect(key1 = inProgressEventDetailState.liveEventData) {
        if (!inProgressEventDetailState.isLoadingLiveEventData && inProgressEventDetailState.liveEventData != null) {
            coroutineScope.launch {
                announcementItemsScrollState.scrollToItem(inProgressEventDetailState.liveEventData!!.announcementItems.size)
            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colors.surface,
                titleContentColor = MaterialTheme.colors.onBackground,
            ),

            title = {
                Text(
                    stringResource(Res.string.in_progress_event_detail_screen__title),
                    style = MaterialTheme.typography.h3,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onBackground
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    component.onEvent(InProgressEventDetailScreenEvent.OnNavigateBack)
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
//        if (!stateEventDetail.isLoadingEventData) {
//            BottomBarWithActions(stateEventDetail.userPermissions!!, component)
//        }
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background
        ) {
            Spacer(Modifier.navigationBarsPadding().height(80.dp))
        }

    }) { paddingValues ->
        if (!inProgressEventDetailState.isLoadingLiveEventData) {
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    Modifier.padding(
                        top = 32.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = paddingValues.calculateBottomPadding()
                    ), verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    if (inProgressEventDetailState.liveEventData != null) {
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Announcements",
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )
                            Column(
                                Modifier.fillMaxWidth()
                                    .background(
                                        MaterialTheme.colors.surface,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(16.dp),
                            )
                            {
                                LazyColumn(
                                    state = announcementItemsScrollState,
                                    modifier = Modifier.fillMaxWidth().height(300.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp),

                                    ) {
                                    itemsIndexed(
                                        inProgressEventDetailState.liveEventData?.announcementItems
                                            ?: emptyList()
                                    ) { index, item ->
                                        AnnouncementItem(
                                            isLatest = index == inProgressEventDetailState.liveEventData!!.announcementItems.size - 1,
                                            announcementItem = item
                                        )
                                    }
                                }

                                Spacer(Modifier.height(24.dp))

                                Column(
                                    Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilledInput(
                                        value = "",
                                        label = "New message",
                                        onValueChange = {}
                                    )
                                    Column {
                                        ButtonPrimary(
                                            type = ColorVariation.APPLE,
                                            text = "Send message",
                                            onClick = {}
                                        )
                                    }
                                }
                            }


                        }
                    }

                    if (inProgressEventDetailState.permissions!!.displayWorkers && !inProgressEventDetailState.isLoadingAttendanceData) {
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(Res.string.in_progress_event_detail_screen__title),
                                    style = MaterialTheme.typography.h2,
                                    color = MaterialTheme.colors.onBackground
                                )
                                Text(
                                    text = stringResource(Res.string.present) +
                                            ": " +
                                            inProgressEventDetailState.attendanceData!!.workers.count {
                                                it.presenceStatus == PresenceStatus.PRESENT
                                            } +
                                            "/" +
                                            inProgressEventDetailState.attendanceData!!.workers.size,
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                            Column(
                                Modifier.fillMaxWidth()
                                    .background(
                                        MaterialTheme.colors.surface,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(16.dp),
                            )
                            {
                                Column(
                                    Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    inProgressEventDetailState.attendanceData!!.workers.forEach { it ->
                                        WorkerBox(it)
                                    }
                                }
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
fun AnnouncementItem(isLatest: Boolean, announcementItem: AnnouncementItemDto) {
    val colorVariation = getButtonColors(ColorVariation.LEMON)
    val date = announcementItem.createdAt.toLocalDateTime(TimeZone.UTC)

    val backgroundColor =
        if (isLatest) colorVariation.backgroundColor else MaterialTheme.colors.surface

    Row(
        Modifier.fillMaxWidth().background(
            backgroundColor, RoundedCornerShape(8.dp)
        ).padding(
            start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.organiser),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onBackground
                )

                Text(
                    text = "${date.hour.toString().padStart(2, '0')}:${
                        date.minute.toString().padStart(2, '0')
                    }",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )
            }

            Text(
                text = announcementItem.message,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondary
            )
        }
    }

}

@Composable
fun WorkerBox(
    worker: AttendanceWorkerDto
) {
    val content = printifyPresence(worker.presenceStatus, worker)
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
                text = content.text,
                style = MaterialTheme.typography.body2,
                color = content.color
            )
        }
    }
}