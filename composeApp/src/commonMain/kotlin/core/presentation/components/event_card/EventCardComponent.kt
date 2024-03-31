package core.presentation.components.event_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.data.helpers.round
import core.data.remote.dto.EventCardDto
import core.domain.event.EventStatus
import core.domain.event.SallaryType
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_card__per_hour_shortcut
import grabit.composeapp.generated.resources.event_card__status_archived
import grabit.composeapp.generated.resources.event_card__status_cancelled
import grabit.composeapp.generated.resources.event_card__status_in_progress
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.LightLime
import ui.theme.Shapes

@Composable
@OptIn(ExperimentalResourceApi::class)
fun printifySallary(event: EventCardDto): String {
    val sallaryType = event.sallaryType
    val amount = round(event.sallaryAmount, 3)

    var ret = ""
    val hourShortcut = stringResource(Res.string.event_card__per_hour_shortcut)
    if (sallaryType == SallaryType.MONEY) {
        ret = "$amount €"
    } else {
        ret = "${event.sallaryProductName} - $amount ${event.sallaryUnit}"
    }
    return "$ret / $hourShortcut"
}

data class EventStatusContent(
    val text: String = "",
    val backgroundColor: Color,
    val color: Color,
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EventStatusTag(event: EventCardDto) {
    val content = when (event.status) {
        EventStatus.CREATED -> EventStatusContent(
            "",
            backgroundColor = Color.Transparent,
            color = Color.Transparent
        )

        EventStatus.PROGRESS -> EventStatusContent(
            stringResource(Res.string.event_card__status_in_progress),
            backgroundColor = LightLime,
            color = Color.Black
        )
        EventStatus.ARCHIVED -> EventStatusContent(
            stringResource(Res.string.event_card__status_archived),
            backgroundColor = Color.LightGray,
            color = Color.Black
        )
        EventStatus.CANCELLED -> EventStatusContent(
            stringResource(Res.string.event_card__status_cancelled),
            backgroundColor = MaterialTheme.colors.error,
            color = Color.White
        )
    }

    if(event.status !== EventStatus.CREATED){
        Box(
            Modifier
                .clip(Shapes.small)
                .background(content.backgroundColor)
        ) {
            Text(
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
                text = content.text,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Normal,
                color = content.color
            )
        }
    }
}

fun printifyEventLocation(event:EventCardDto):String{
    val ret = ArrayList<String>()

    if(event.location.name !== null){
        ret.add(event.location.name)
    }
    ret.add(event.location.city)
    return ret.joinToString(separator = ", ")
}

fun printifyEventDateTime(event:EventCardDto):String{
    val date = event.happeningAt.toLocalDateTime(TimeZone.UTC)

    return "${date.dayOfMonth}. ${date.monthNumber}. ${date.year}, ${date.hour}:${date.minute}"
}
