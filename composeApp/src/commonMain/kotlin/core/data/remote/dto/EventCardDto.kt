package core.data.remote.dto

import EventCategoryDto
import LocationDto
import core.domain.event.EventStatus
import core.domain.event.SallaryType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.domain.ColorVariation


@Serializable
data class EventCardDto(
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
    @SerialName("User")
    val user: User
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