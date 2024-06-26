package core.presentation.components.event_card

import SallaryObject
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.data.helpers.event.printifyEventDateTime
import core.data.helpers.event.printifyEventLocation
import core.data.remote.dto.EventCardDto
import core.presentation.components.event_categories.EventCategories
import core.presentation.components.event_image.EventImage
import core.presentation.components.event_image.ImagePlaceholder
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.location
import grabit.composeapp.generated.resources.logo
import grabit.composeapp.generated.resources.time_circle
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import printifySallary
import ui.theme.DarkCardBody
import ui.theme.LightCardBody
import ui.theme.Shapes

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterialApi::class)
@Composable
fun EventCard(
    event: EventCardDto,
    onClick: (id: String) -> Unit,
    onStatusTagClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBodyBackground = if (isSystemInDarkTheme()) DarkCardBody else LightCardBody

    Card(
        onClick = { onClick(event.id) },
        elevation = 0.dp,
        backgroundColor = cardBodyBackground,
        modifier = modifier.then(
            if (!isSystemInDarkTheme()) {
                Modifier.border(
                    width = 1.dp, color = Color(0x4DE5E5E5), shape = RoundedCornerShape(12.dp)
                ).shadow(
                    elevation = 12.dp,
                    spotColor = Color(0xFFE6E6E6),
                    ambientColor = Color(0xFFE5E5E5),
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Modifier
            }
        )
    ) {
        Box(Modifier.padding(8.dp).fillMaxWidth()) {
            Column {
                Box {
                    if (event.thumbnailURL != null) {
                        EventImage(event.thumbnailURL)
                    } else {
                        ImagePlaceholder()
                    }

                    Box(
                        Modifier.padding(start = 8.dp, bottom = 12.dp).clip(Shapes.small)
                            .background(MaterialTheme.colors.background)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp
                            ),
                            text = printifySallary(
                                SallaryObject(
                                    event.sallaryType,
                                    event.sallaryAmount,
                                    event.sallaryProductName,
                                    event.sallaryUnit,
                                )
                            ),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Box(Modifier.align(Alignment.TopEnd).padding(end = 8.dp, top = 8.dp)) {
                        EventStatusTag(event.status, onStatusTagClick = {
                            onStatusTagClick()
                        })
                    }
                }

                Column(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 24.dp)) {
                    Text(
                        event.name,
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(Modifier.height(12.dp))
                    EventCategories(event.eventCategoryRelation.map { categoryRelation -> categoryRelation.eventCategory })
                    Spacer(Modifier.height(24.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = vectorResource(Res.drawable.location),
                                contentDescription = stringResource(Res.string.logo),
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                printifyEventLocation(event.location),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = vectorResource(Res.drawable.time_circle),
                                contentDescription = stringResource(Res.string.logo),
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                printifyEventDateTime(event.happeningAt),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}