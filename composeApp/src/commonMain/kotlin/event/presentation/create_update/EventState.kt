package event.presentation.create_update

import core.data.remote.dto.EventCategoryDto
import LocationDto
import core.domain.event.SallaryType
import event.data.dto.EventCreateUpdateDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable

@Serializable
data class EventState(
    val imageUrl: String?,
    val title: String,
    val description: String,
    val capacity: String,
    val date: LocalDate?,
    val time: LocalTime?,
    val requiredTools: String,
    val providedTools: String,
    val searchLocation: String,
    val location: LocationDto?,
    val salaryType: SallaryType,
    val salaryAmount: String,
    val salaryUnit: String,
    val salaryGoodTitle: String,
    val searchCategory: String,
    val categoryList: MutableList<EventCategoryDto>
)

fun EventState.toEvent(eventState: EventState): EventCreateUpdateDto {
    return EventCreateUpdateDto(
        thumbnailURL = eventState.imageUrl,
        name = eventState.title,
        description = eventState.description,
        capacity = eventState.capacity.toInt(),
        happeningAt = LocalDateTime(eventState.date!!, eventState.time!!).toInstant(TimeZone.UTC),
        toolingRequired = eventState.requiredTools,
        toolingProvided = eventState.providedTools,
        location = LocationDto(
            address = "",
            city = "",
            locationLat = 0.0,
            locationLon = 0.0,
            name = eventState.searchLocation
        ),
        sallaryType = eventState.salaryType,
        sallaryAmount = eventState.salaryAmount,
        sallaryUnit = eventState.salaryUnit,
        sallaryProductName = eventState.salaryGoodTitle,
        categories = eventState.categoryList.map {
            it.name
        }
    )
}
