package event.presentation.event_detail_live

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material.SnackbarHostState
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.helpers.printifyTime
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import core.presentation.components.offline_message.OfflineMessage
import event.data.dto.AnnouncementItemDto
import event.data.dto.AttendanceDataDto
import event.data.dto.AttendanceWorkerDto
import event.data.dto.WorkerUserDto
import event.domain.model.PresenceStatus
import event.presentation.event_detail.component.EventDetailScreenEvent
import event.presentation.event_detail_live.component.InProgressEventDetailScreenComponent
import event.presentation.event_detail_live.component.InProgressEventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_detail_screen__cannot_register_as_organiser
import grabit.composeapp.generated.resources.event_detail_screen__capacity_full
import grabit.composeapp.generated.resources.event_detail_screen__edit
import grabit.composeapp.generated.resources.event_detail_screen__end_harvest
import grabit.composeapp.generated.resources.event_detail_screen__end_harvest_notice
import grabit.composeapp.generated.resources.event_detail_screen__sign_in_for_harvest
import grabit.composeapp.generated.resources.event_detail_screen__sign_off_harvest
import grabit.composeapp.generated.resources.event_detail_screen__start_event
import grabit.composeapp.generated.resources.event_detail_screen__you_are_signed_in
import grabit.composeapp.generated.resources.eye
import grabit.composeapp.generated.resources.in_progress_event_detail_announcement_message
import grabit.composeapp.generated.resources.in_progress_event_detail_no_announcements
import grabit.composeapp.generated.resources.in_progress_event_detail_publish_announcement
import grabit.composeapp.generated.resources.in_progress_event_detail_screen__title
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__discard
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__last_updated
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__update
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_manage_attendance
import grabit.composeapp.generated.resources.left
import grabit.composeapp.generated.resources.offline_message_title
import grabit.composeapp.generated.resources.organiser
import grabit.composeapp.generated.resources.present
import grabit.composeapp.generated.resources.workers
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import printifyPresence
import ui.data.getButtonColors
import ui.domain.ButtonColors
import ui.domain.ColorVariation
import ui.theme.LightOnOrange
import ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun InProgressEventDetailScreen(component: InProgressEventDetailScreenComponent) {
    val announcementItemsScrollState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val inProgressEventDetailState by component.inProgressEventDetailState.subscribeAsState()
    val announcementText by component.announcementText.subscribeAsState()


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
            if (!inProgressEventDetailState.isLoadingLiveEventData && inProgressEventDetailState.permissions!!.displayEndEvent && !inProgressEventDetailState.isOffline) {
                BottomNavigation(
                    elevation = 16.dp,
                    modifier = Modifier.padding(0.dp)
                ) {

                    Box(
                        Modifier.background(MaterialTheme.colors.background).navigationBarsPadding()
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = 24.dp,
                                bottom = 24.dp,
                            ), Alignment.BottomCenter
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ButtonPrimary(type = ColorVariation.CHERRY,
                                text = stringResource(Res.string.event_detail_screen__end_harvest),
                                onClick = {
                                    component.onEvent(InProgressEventDetailScreenEvent.EndEvent)
                                }
                            )
                            Text(
                                text = stringResource(Res.string.event_detail_screen__end_harvest_notice),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.secondary
                            )
                        }

                    }
                }
            } else if (!inProgressEventDetailState.isLoadingAttendanceData && inProgressEventDetailState.isOffline) {
                BottomNavigation(
                    elevation = 16.dp,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Box(
                        Modifier.fillMaxWidth().background(MaterialTheme.colors.background)
                            .navigationBarsPadding()
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = 24.dp,
                                bottom = 24.dp,
                            ), Alignment.BottomCenter
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                stringResource(Res.string.offline_message_title),
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
            }

        }) { paddingValues ->

        Column(
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier.padding(
                    top = 32.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = paddingValues.calculateBottomPadding() + 24.dp
                ), verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (inProgressEventDetailState.isOffline) {
                    OfflineMessage()
                }
                if (!inProgressEventDetailState.isLoadingLiveEventData && inProgressEventDetailState.liveEventData != null && !inProgressEventDetailState.isOffline ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.in_progress_event_detail_publish_announcement),
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
                                if (inProgressEventDetailState.liveEventData?.announcementItems?.isEmpty() == true) {
                                    item {
                                        Text(
                                            stringResource(Res.string.in_progress_event_detail_no_announcements),
                                            style = MaterialTheme.typography.body1,
                                            color = MaterialTheme.colors.onBackground
                                        )
                                    }
                                }
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

                            if (inProgressEventDetailState.permissions!!.displayPublishAnnouncement) {
                                Column(
                                    Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilledInput(
                                        value = announcementText,
                                        label = stringResource(Res.string.in_progress_event_detail_announcement_message),
                                        onValueChange = {
                                            component.onEvent(
                                                InProgressEventDetailScreenEvent.AnnouncementInputChange(
                                                    it
                                                )
                                            )
                                        }
                                    )
                                    Row(horizontalArrangement = Arrangement.End) {
                                        ButtonPrimary(
                                            buttonModifier = Modifier.wrapContentSize(),
                                            type = ColorVariation.APPLE,
                                            text = stringResource(Res.string.in_progress_event_detail_publish_announcement),
                                            onClick = {

                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (inProgressEventDetailState.permissions!!.displayWorkers && !inProgressEventDetailState.isLoadingAttendanceData && inProgressEventDetailState.attendanceData != null) {
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
                                text = stringResource(Res.string.in_progress_event_detail_screen_attendance),
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onBackground
                            )
                            Text(
                                text = stringResource(Res.string.present) +
                                        ": " +
                                        inProgressEventDetailState.updatedAttendanceData!!.workers.count {
                                            it.presenceStatus == PresenceStatus.PRESENT
                                        } +
                                        "/" +
                                        inProgressEventDetailState.updatedAttendanceData!!.workers.size,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onBackground
                            )
                        }
                        Column(
                            Modifier.fillMaxWidth()
                                .background(
                                    MaterialTheme.colors.surface,
                                    RoundedCornerShape(14.dp)
                                ).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)

                        )
                        {
                            Column(
                                Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    "${stringResource(Res.string.in_progress_event_detail_screen_attendance__last_updated)}: ${
                                        printifyTime(
                                            inProgressEventDetailState.attendanceData!!.lastUpdated.toLocalDateTime(
                                                TimeZone.currentSystemDefault()
                                            )
                                        )
                                    }"
                                )
                                inProgressEventDetailState.updatedAttendanceData!!.workers.forEach { it ->
                                    WorkerBox(it, onClick = { id, status ->
                                        component.onEvent(
                                            InProgressEventDetailScreenEvent.ModifyAttendance(
                                                id,
                                                status
                                            )
                                        )
                                    })
                                }
                            }

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ButtonPrimary(
                                    buttonModifier = Modifier.weight(0.6f),
                                    type = ColorVariation.CHERRY,
                                    text = stringResource(Res.string.in_progress_event_detail_screen_attendance__discard),
                                    onClick = {
                                        component.onEvent(InProgressEventDetailScreenEvent.DiscardChanges)
                                    },
                                    enabled = inProgressEventDetailState.isAttendanceUpdated,
                                )

                                ButtonPrimary(
                                    buttonModifier = Modifier.weight(0.4f),
                                    type = ColorVariation.APPLE,
                                    text = stringResource(Res.string.in_progress_event_detail_screen_attendance__update),
                                    isLoading = inProgressEventDetailState.isLoadingAttendanceUpdate,
                                    onClick = {
                                        component.onEvent(InProgressEventDetailScreenEvent.SaveAttendance)
                                    }
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WorkerBox(
    worker: AttendanceWorkerDto, onClick: (id: String, status: PresenceStatus) -> Unit,
) {
    val content = printifyPresence(worker.presenceStatus, worker)
    val colorPresent = getButtonColors(ColorVariation.LIME)
    val colorLeave = getButtonColors(ColorVariation.CHERRY)
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
        Column {
            if (worker.presenceStatus === PresenceStatus.PRESENT) {
                IconButton(
                    modifier = Modifier.padding(2.dp).background(
                        colorLeave.backgroundColor,
                        Shapes.medium
                    ),
                    onClick = {
                        onClick(worker.user.id, PresenceStatus.LEFT)
                    }
                ) {
                    Icon(
                        tint = colorLeave.textColor,
                        modifier = Modifier.size(24.dp),
                        imageVector = vectorResource(Res.drawable.left),
                        contentDescription = null,
                    )
                }
            } else if (worker.presenceStatus === PresenceStatus.NOT_PRESENT) {
                IconButton(
                    modifier = Modifier.padding(2.dp).background(
                        colorPresent.backgroundColor,
                        Shapes.medium
                    ),
                    onClick = {
                        onClick(worker.user.id, PresenceStatus.PRESENT)
                    }
                ) {
                    Icon(
                        tint = colorPresent.textColor,
                        modifier = Modifier.size(24.dp),
                        imageVector = vectorResource(Res.drawable.present),
                        contentDescription = null,
                    )
                }
            } else {
                Box(Modifier.height(52.dp))
            }
        }
    }
}