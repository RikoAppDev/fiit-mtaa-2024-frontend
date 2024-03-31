package event_detail.data.dto


import EventCategoryDto
import LocationDto
import core.domain.event.EventStatus
import core.domain.event.SallaryType
import kotlinx.serialization.*
import ui.domain.ColorVariation

@Serializable
data class EventDetailDto (
    val capacity: Long,
    val description: String,

    @SerialName("EventCategoryRelation")
    val eventCategoryRelation: List<EventCategoryRelation>,

    val happeningAt: String,
    val id: String,

    @SerialName("Location")
    val location: LocationDto,

    val name: String,
    val sallaryAmount: Double,
    val sallaryProductName: String? = null,
    val sallaryType: SallaryType,
    val sallaryUnit: String? = null,
    val status: EventStatus,
    val thumbnailURL: String? = null,
    val toolingProvided: String? = null,
    val toolingRequired: String? = null,

    @SerialName("User")
    val user: User,

    @SerialName("_count")
    val count:EventAssignment
)

@Serializable data class EventAssignment(
    @SerialName("EventAssignment")
    val eventAssignment: Long
)


@Serializable
data class EventCategoryRelation (
    /**
     * ID
     */
    @SerialName("EventCategory")
    val eventCategory: EventCategoryDto
)

@Serializable
data class User (
    val name: String
)