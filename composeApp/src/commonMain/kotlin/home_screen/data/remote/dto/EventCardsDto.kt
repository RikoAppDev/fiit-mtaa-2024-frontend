// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json     = Json { allowStructuredMapKeys = true }
// val response = json.parse(Response.serializer(), jsonString)

import auth.data.remote.dto.AuthUserDto
import auth.domain.model.AccountType
import core.domain.event.EventStatus
import core.domain.event.SallaryType
import kotlinx.serialization.*

@Serializable
data class EventsCardListDto (
    val events: List<EventCard>
)


@Serializable
data class EventCard (
    val capacity: Long,

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
    @SerialName("EventCategory")
    val eventCategory: EventCategory
)

@Serializable
data class EventCategory (
    val icon: String,
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