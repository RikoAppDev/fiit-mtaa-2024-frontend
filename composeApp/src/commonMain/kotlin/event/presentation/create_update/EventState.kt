package event.presentation.create_update

import event.domain.model.Event
import event.domain.model.EventLocation
import event.domain.model.SalaryType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class EventState(
    val image: ByteArray?,
    val title: String,
    val description: String,
    val capacity: String,
    val date: LocalDate?,
    val time: LocalTime?,
    val requiredTools: String,
    val providedTools: String,
    val searchLocation: String,
    val salaryType: SalaryType,
    val salaryAmount: String,
    val salaryUnit: String,
    val salaryGoodTitle: String,
    val searchCategory: String,
    val categoryList: MutableList<String>
)

fun EventState.toEvent(eventState: EventState): Event {
    return Event(
        image = eventState.image,
        title = eventState.title,
        description = eventState.description,
        capacity = eventState.capacity.toInt(),
        datetime = LocalDateTime(eventState.date!!, eventState.time!!),
        requiredTools = eventState.requiredTools,
        providedTools = eventState.providedTools,
        location = EventLocation(eventState.searchLocation, 0.0, 0.0),
        salaryType = eventState.salaryType,
        salaryAmount = eventState.salaryAmount,
        salaryUnit = eventState.salaryUnit,
        salaryGoodTitle = eventState.salaryGoodTitle,
        categoryList = eventState.categoryList
    )
}
