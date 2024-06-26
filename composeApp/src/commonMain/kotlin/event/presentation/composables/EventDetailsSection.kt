package event.presentation.composables

import SallaryObject
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import core.data.helpers.event.printifyEventDateTime
import core.domain.event.EventStatus
import core.presentation.components.event_card.EventStatusTag
import core.presentation.components.event_categories.EventCategories
import core.presentation.components.event_image.EventImage
import core.presentation.components.event_image.ImagePlaceholder
import event.data.dto.EventDetailDto
import event.presentation.event_detail.InfoRow
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.categories
import grabit.composeapp.generated.resources.event_detail_screen__capacity_full
import grabit.composeapp.generated.resources.event_detail_screen__free_places
import grabit.composeapp.generated.resources.event_reporting_available_text_harvester
import grabit.composeapp.generated.resources.event_reporting_available_text_organiser
import grabit.composeapp.generated.resources.event_reporting_available_title_harvester
import grabit.composeapp.generated.resources.event_reporting_available_title_organiser
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
import grabit.composeapp.generated.resources.top_bar_navigation__back
import home_screen.presentation.component.HomeScreenEvent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import printifySallary
import ui.theme.LightOnOrange
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EventDetailsSection(
    event: EventDetailDto,
    onStatusTagClick: () -> Unit,
    onReportingClick: () -> Unit,
) {
    val freeCapacity = event.capacity - event.count.eventAssignment
    val isFreeCapacity = freeCapacity > 0
    Column {
        Box {
            if (event.thumbnailURL != null) {
                EventImage(event.thumbnailURL)
            } else {
                ImagePlaceholder()
            }

            Box(Modifier.align(Alignment.TopEnd).padding(end = 8.dp, top = 8.dp)) {
                EventStatusTag(event.status, onStatusTagClick = {
                    onStatusTagClick()
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = event.name,
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = event.description,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondary
        )


        if (event.status === EventStatus.ARCHIVED) {
            val title =
                if (event.isOwnedByUser) stringResource(Res.string.event_reporting_available_title_organiser) else stringResource(
                    Res.string.event_reporting_available_title_harvester
                )

            val text =
                if (event.isOwnedByUser) stringResource(Res.string.event_reporting_available_text_organiser) else stringResource(
                    Res.string.event_reporting_available_text_harvester
                )
            Spacer(Modifier.height(24.dp))
            Row(
                Modifier.clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth().clickable {
                        onReportingClick()
                    }
                    .background(MaterialTheme.colors.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondary
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = LightOnOrange
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            InfoRow(
                stringResource(Res.string.capacity),
                Res.drawable.profile,
                text =
                if (isFreeCapacity) "${stringResource(Res.string.event_detail_screen__free_places)}: $freeCapacity"
                else
                    stringResource(Res.string.event_detail_screen__capacity_full)
            )

            InfoRow(
                stringResource(Res.string.organizer),
                Res.drawable.home,
                event.user.name
            )

            InfoRow(
                stringResource(Res.string.starts_at),
                Res.drawable.time_circle,
                printifyEventDateTime(event.happeningAt)
            )

            if ((event.toolingRequired !== null && event.toolingRequired != "") || (event.toolingProvided !== null && event.toolingProvided != "")) {
                Column {
                    Text(
                        text = stringResource(Res.string.tooling),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (event.toolingProvided !== null && event.toolingProvided != "") {
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
                                        text = event.toolingProvided.toString(),
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }
                        }

                        if (event.toolingRequired !== null && event.toolingRequired != "") {
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
                                        text = event.toolingRequired.toString(),
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
                text = "${event.location.name}\n${event.location.address}\n${event.location.city}"
            )

            InfoRow(
                stringResource(Res.string.salary),
                Res.drawable.sallary,
                text = printifySallary(
                    SallaryObject(
                        event.sallaryType,
                        event.sallaryAmount,
                        event.sallaryProductName,
                        event.sallaryUnit,
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
                    EventCategories(event.eventCategoryRelation.map { categoryRelation ->
                        categoryRelation.eventCategory
                    })
                }
            }
        }
    }
}