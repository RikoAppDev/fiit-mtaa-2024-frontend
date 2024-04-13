package event.presentation.composables

import SallaryObject
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import core.data.helpers.event.printifyEventDateTime
import core.presentation.components.event_card.EventStatusTag
import core.presentation.components.event_categories.EventCategories
import event.data.dto.EventDetailDto
import event.presentation.event_detail.InfoRow
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.capacity
import grabit.composeapp.generated.resources.categories
import grabit.composeapp.generated.resources.event_detail_screen__capacity_full
import grabit.composeapp.generated.resources.event_detail_screen__free_places
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import printifySallary
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EventDetailsSection(event: EventDetailDto) {
    val freeCapacity = event.capacity - event.count.eventAssignment
    val isFreeCapacity = freeCapacity > 0
    Column {
        Box {
            AsyncImage(
                modifier = Modifier.height(196.dp).clip(Shapes.medium),
                model = event.thumbnailURL,
                contentDescription = null,
                imageLoader = ImageLoader(LocalPlatformContext.current),
                contentScale = ContentScale.Crop,
            )

            Box(Modifier.align(Alignment.TopEnd).padding(end = 8.dp, top = 8.dp)) {
                EventStatusTag(event.status)
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

            if (event.toolingRequired !== null || event.toolingProvided !== null) {
                Column {
                    Text(
                        text = stringResource(Res.string.tooling),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (event.toolingProvided !== null) {
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

                        if (event.toolingRequired !== null) {
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