package event.presentation.event_detail_live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import event.presentation.event_detail_live.component.InProgressEventDetailScreenComponent
import event.presentation.event_detail_live.component.InProgressEventDetailScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.in_progress_event_detail_screen__title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.data.getButtonColors
import ui.domain.ColorVariation
import ui.theme.LightOnOrange

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun InProgressEventDetailScreen(component: InProgressEventDetailScreenComponent) {
    val announcementItemsScrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        // Scrolls to the bottom when the composable is first displayed
        announcementItemsScrollState.scrollTo(announcementItemsScrollState.maxValue)
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val announcementItems = listOf(4, 2, 1, 7, 11, 4, 3, 2, 1)

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
    }) { paddingValues ->

        Column(
            Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Announcements",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground
                )
                Column(
                    Modifier.fillMaxWidth()
                        .background(MaterialTheme.colors.surface, RoundedCornerShape(14.dp))
                        .padding(16.dp),
                )
                {
                    Column(
                        Modifier.fillMaxWidth().height(300.dp)
                            .verticalScroll(announcementItemsScrollState),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        announcementItems.forEachIndexed { index, i ->
                            AnnouncementItem(index == announcementItems.size - 1)
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
                        Column() {
                            ButtonPrimary(
//                                buttonModifier = Modifier.weight(0.5f),
                                type = ColorVariation.APPLE,
                                text = "Send message",
                                onClick = {}
                            )
                        }
                    }
                }


            }

        }
    }

}


@Composable
fun AnnouncementItem(isLatest: Boolean) {
    val colorVariation = getButtonColors(ColorVariation.LIME)

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
                    text = "Organizátor",
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onBackground
                )

                Text(
                    text = "12:27",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )
            }

            Text(
                text = "This is some important announcement, this is a longer one",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondary
            )
        }
    }

}