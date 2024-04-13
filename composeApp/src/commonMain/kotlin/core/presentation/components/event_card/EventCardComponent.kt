package core.presentation.components.event_card

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.domain.event.EventStatus
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.event_card__status_archived
import grabit.composeapp.generated.resources.event_card__status_cancelled
import grabit.composeapp.generated.resources.event_card__status_in_progress
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.theme.LightLime
import ui.theme.Shapes

data class EventStatusContent(
    val text: String = "",
    val backgroundColor: Color,
    val color: Color,
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EventStatusTag(eventStatus: EventStatus, onStatusTagClick: () -> Unit) {
    val content = when (eventStatus) {
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

    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    if (eventStatus !== EventStatus.CREATED) {
        Row(
            modifier = Modifier
                .clip(Shapes.small)
                .background(content.backgroundColor)
                .clickable(enabled = eventStatus == EventStatus.PROGRESS) {
                    onStatusTagClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.scale(scale)) {
                Surface(
                    color = Color.Red,
                    shape = CircleShape,
                    modifier = Modifier.size(8.dp),
                    content = {}
                )
            }
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
