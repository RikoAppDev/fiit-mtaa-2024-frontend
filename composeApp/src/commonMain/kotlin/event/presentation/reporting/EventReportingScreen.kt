package event.presentation.reporting

import androidx.compose.material.Text
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import auth.domain.model.AccountType
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.data.helpers.event.printifyExpense
import core.data.helpers.printifyTime
import event.presentation.reporting.component.EventReportingScreenComponent
import event.presentation.reporting.component.EventReportingScreenEvent
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.all_events_screen__not_events_found
import grabit.composeapp.generated.resources.event_reporting_available_title_harvester
import grabit.composeapp.generated.resources.event_reporting_available_title_organiser
import grabit.composeapp.generated.resources.event_reporting_hours_in_total
import grabit.composeapp.generated.resources.event_reporting_hours_worker
import grabit.composeapp.generated.resources.event_reporting_price_per_hour
import grabit.composeapp.generated.resources.event_reporting_screen_not_found
import grabit.composeapp.generated.resources.event_reporting_screen_title
import grabit.composeapp.generated.resources.event_reporting_summary
import grabit.composeapp.generated.resources.event_reporting_total_expenses
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__arrived_at
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__left_at
import grabit.composeapp.generated.resources.register_screen__role_organiser_title
import grabit.composeapp.generated.resources.salary
import grabit.composeapp.generated.resources.top_bar_navigation__back
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import printifySallary
import ui.theme.LightOnOrange
import ui.theme.MenuActive
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventReportingScreen(
    component: EventReportingScreenComponent,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val stateReporting by component.stateReporting.subscribeAsState()

    LaunchedEffect(true) {
        component.loadReporting()
    }
    val title =
        if (stateReporting.user.accountType == AccountType.ORGANISER.toString()) stringResource(Res.string.event_reporting_available_title_organiser) else stringResource(
            Res.string.event_reporting_available_title_harvester
        )

    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.surface,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text(
                        stringResource(Res.string.event_reporting_screen_title),
                        style = MaterialTheme.typography.h3,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        component.onEvent(EventReportingScreenEvent.OnNavigateBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.top_bar_navigation__back),
                            tint = LightOnOrange
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            if (stateReporting.isLoading) {
                Spacer(Modifier.height(32.dp))
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center),
                        color = MaterialTheme.colors.secondary,
                        strokeWidth = 3.dp,
                    )
                }
            } else {


                Column(
                    Modifier.background(MaterialTheme.colors.background)
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(Modifier.height(12.dp))

                    if (stateReporting.notFound) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(Res.string.event_reporting_screen_not_found),
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            stateReporting.reporting?.reportingItems?.forEach {
                                Column(
                                    Modifier.fillMaxWidth()
                                        .background(
                                            MaterialTheme.colors.surface,
                                            shape = Shapes.medium
                                        )
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it.user.name,
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.onBackground
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    TableRow(
                                        stringResource(Res.string.event_reporting_hours_worker),
                                        "${it.hoursWorked}h"
                                    )
                                    TableRow(
                                        stringResource(Res.string.in_progress_event_detail_screen_attendance__arrived_at),
                                        printifyTime(it.arrivedAt.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()))
                                    )
                                    TableRow(
                                        stringResource(Res.string.in_progress_event_detail_screen_attendance__left_at),
                                        printifyTime(it.leftAt.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()))
                                    )
                                    TableRow(
                                        stringResource(Res.string.salary),
                                        printifyExpense(
                                            sallary = stateReporting.reporting!!.sallary,
                                            hours = it.hoursWorked
                                        ),
                                        true
                                    )
                                }
                            }
                            if (stateReporting.user.accountType == AccountType.ORGANISER.toString()) {
                                val hoursTotal =
                                    stateReporting.reporting!!.reportingItems.sumOf { it.hoursWorked }
                                Spacer(Modifier.height(32.dp))
                                Column {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(Res.string.event_reporting_summary),
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.onBackground
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Column(
                                        Modifier.fillMaxWidth()
                                            .background(
                                                MaterialTheme.colors.surface,
                                                shape = Shapes.medium
                                            )
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        TableRow(
                                            stringResource(Res.string.event_reporting_hours_in_total),
                                            "$hoursTotal h"
                                        )
                                        TableRow(
                                            stringResource(Res.string.event_reporting_price_per_hour),
                                            printifySallary(stateReporting.reporting!!.sallary)
                                        )

                                        TableRow(
                                            stringResource(Res.string.event_reporting_total_expenses),
                                            printifyExpense(
                                                stateReporting.reporting!!.sallary,
                                                hoursTotal
                                            ),
                                            true
                                        )
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


@Composable
fun TableRow(heading: String, text: String, isSalary: Boolean = false) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(4f),
            text = heading,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            modifier = Modifier.weight(3f),
            text = text,
            style = MaterialTheme.typography.body1,
            color = if (isSalary) MenuActive else MaterialTheme.colors.secondary
        )
    }

}