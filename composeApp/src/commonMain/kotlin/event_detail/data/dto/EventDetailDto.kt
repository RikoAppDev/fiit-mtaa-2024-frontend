package event_detail.data.dto


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
    val location: Location,

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
    val user: User
)

@Serializable
data class EventCategoryRelation (
    /**
     * ID
     */
    @SerialName("EventCategory")
    val eventCategory: EventCategory
)


@Serializable
data class EventCategory (
    val colorVariant: ColorVariation,
    val icon: String,
    val id: String,
    val name: String
)
@Serializable
data class Location (
    val address: String,
    val city: String,
    val locationLat: Double,
    val locationLon: Double,
    val name: String? = null
)


@Serializable
data class User (
    val name: String
)