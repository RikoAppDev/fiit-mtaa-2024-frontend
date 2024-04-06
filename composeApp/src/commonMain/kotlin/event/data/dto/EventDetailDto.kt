package event.data.dto

import EventCategoryDto
import LocationDto
import core.domain.event.EventStatus
import core.domain.event.SallaryType
import kotlinx.datetime.Instant
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
    val isOwnedByUser:Boolean,
    val isUserSignedIn:Boolean,

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