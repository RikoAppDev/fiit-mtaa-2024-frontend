package core.data.remote.dto

import core.domain.event.EventStatus
import core.domain.event.SallaryType
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.domain.ColorVariation

@Serializable
data class EventCardDto(
    @SerialName("EventCategoryRelation")
    val eventCategoryRelation: List<EventCategoryRelation>,

    val happeningAt: Instant,
    val id: String,

    @SerialName("Location")
    val location: Location,

    val name: String,
    val sallaryAmount: Double,
    val sallaryProductName: String?,
    val sallaryType: SallaryType,
    val sallaryUnit: String?,
    val status: EventStatus,
    val thumbnailURL: String?,
    @SerialName("User")
    val user: User
)

@Serializable
data class EventCategoryRelation(
    @SerialName("EventCategory")
    val eventCategory: EventCategory
)

@Serializable
data class EventCategory(
    val icon: String,
    val name: String,
    val colorVariant: ColorVariation
)

@Serializable
data class Location(
    val address: String,
    val city: String,
    val locationLat: Double,
    val locationLon: Double,
    val name: String?
)

@Serializable
data class User(
    val name: String
)