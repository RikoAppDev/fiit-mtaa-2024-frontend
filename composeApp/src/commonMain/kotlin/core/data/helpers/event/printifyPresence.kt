import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import grabit.composeapp.generated.resources.Res
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import event.data.dto.AttendanceWorkerDto
import event.domain.model.PresenceStatus
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__arrived_at
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__did_not_arrive
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__left_at
import grabit.composeapp.generated.resources.in_progress_event_detail_screen_attendance__not_present
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.data.getButtonColors
import ui.domain.ColorVariation
import ui.theme.MenuActive

data class PrintifyPresenceData(
    val text: String,
    val color: Color
)

@Composable
@OptIn(ExperimentalResourceApi::class, ExperimentalResourceApi::class)
fun printifyPresence(
    presenceStatus: PresenceStatus,
    attendance: AttendanceWorkerDto
): PrintifyPresenceData {
    val text = when (presenceStatus) {
        PresenceStatus.NOT_PRESENT ->
            stringResource(Res.string.in_progress_event_detail_screen_attendance__not_present)

        PresenceStatus.DID_NOT_ARRIVE ->
            stringResource(Res.string.in_progress_event_detail_screen_attendance__did_not_arrive)

        PresenceStatus.PRESENT -> {
            attendance.arrivedAt?.let {
                val dateArrived = it.toLocalDateTime(TimeZone.UTC)
                val formattedTime = "${dateArrived.hour.toString().padStart(2, '0')}:${dateArrived.minute.toString().padStart(2, '0')}"
                stringResource(Res.string.in_progress_event_detail_screen_attendance__arrived_at) + " " + formattedTime
            } ?: stringResource(Res.string.in_progress_event_detail_screen_attendance__not_present)
        }

        PresenceStatus.LEFT -> {
            attendance.leftAt?.let {
                val dateLeft = it.toLocalDateTime(TimeZone.UTC)
                val formattedTime = "${dateLeft.hour.toString().padStart(2, '0')}:${dateLeft.minute.toString().padStart(2, '0')}"
                stringResource(Res.string.in_progress_event_detail_screen_attendance__left_at) + " " + formattedTime
            } ?: stringResource(Res.string.in_progress_event_detail_screen_attendance__not_present)
        }
    }

    val color = when (presenceStatus) {
        PresenceStatus.NOT_PRESENT -> MaterialTheme.colors.secondary
        PresenceStatus.DID_NOT_ARRIVE -> MaterialTheme.colors.error
        PresenceStatus.PRESENT -> Color(0xFF3C8900)
        PresenceStatus.LEFT -> Color(0xFFFFA033)
    }

    return PrintifyPresenceData(text, color)
}
