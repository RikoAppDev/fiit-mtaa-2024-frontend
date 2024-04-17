package event.data.dto

import core.domain.event.SallaryType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EventCreateUpdateDto(
    val thumbnailURL: String?,
    val name: String,
    val description: String?,
    val capacity: Int,
    val happeningAt: Instant,
    val toolingRequired: String?,
    val toolingProvided: String?,
    val placeId: String?,
    val sallaryType: SallaryType,
    val sallaryAmount: Float,
    val sallaryUnit: String?,
    val sallaryProductName: String?,
    val categories: List<String>?
)
