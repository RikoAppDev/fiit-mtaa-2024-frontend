package event.data.dto

import core.data.remote.dto.EventCategoryDto
import LocationDto
import core.domain.event.EventStatus
import core.domain.event.SallaryType
import event.presentation.create_update.EventState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.*

@Serializable
data class EventDetailDto(
    val capacity: Long,
    val description: String,

    @SerialName("EventCategoryRelation")
    val eventCategoryRelation: List<EventCategoryRelation>,

    val happeningAt: Instant,
    val id: String,

    @SerialName("Location")
    val location: LocationDto,

    val name: String,
    val sallaryAmount: Double,
    val sallaryProductName: String?,
    val sallaryType: SallaryType,
    val sallaryUnit: String?,
    val status: EventStatus,
    val thumbnailURL: String?,
    val toolingProvided: String?,
    val toolingRequired: String?,
    val isOwnedByUser: Boolean,
    val isUserSignedIn: Boolean,

    @SerialName("User")
    val user: User,

    @SerialName("_count")
    val count: EventAssignment
)

@Serializable
data class EventAssignment(
    @SerialName("EventAssignment")
    val eventAssignment: Long
)


@Serializable
data class EventCategoryRelation(
    @SerialName("EventCategory")
    val eventCategory: EventCategoryDto
)

@Serializable
data class User(
    val name: String
)

fun EventDetailDto.toEventState(eventDetailDto: EventDetailDto): EventState {
    return EventState(
        imageUrl = eventDetailDto.thumbnailURL,
        title = eventDetailDto.name,
        description = eventDetailDto.description,
        capacity = eventDetailDto.capacity.toString(),
        date = happeningAt.toLocalDateTime(TimeZone.UTC).date,
        time = LocalTime(
            happeningAt.toLocalDateTime(TimeZone.UTC).hour,
            happeningAt.toLocalDateTime(TimeZone.UTC).minute
        ),
        requiredTools = eventDetailDto.toolingRequired ?: "",
        providedTools = eventDetailDto.toolingProvided ?: "",
        placeId = "",
        locationName = eventDetailDto.location.name,
        salaryType = eventDetailDto.sallaryType,
        salaryAmount = eventDetailDto.sallaryAmount.toString(),
        salaryUnit = eventDetailDto.sallaryUnit ?: "",
        salaryGoodTitle = eventDetailDto.sallaryProductName ?: "",
        categoryList = eventDetailDto.eventCategoryRelation.map {
            it.eventCategory
        }.toMutableList()
    )
}