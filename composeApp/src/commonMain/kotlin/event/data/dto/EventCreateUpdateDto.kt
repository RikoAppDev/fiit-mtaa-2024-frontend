package event.data.dto

import LocationDto
import core.domain.event.SallaryType
import kotlinx.datetime.Instant

data class EventCreateUpdateDto(
    val thumbnailURL: String?,
    val name: String,
    val description: String?,
    val capacity: Int,
    val happeningAt: Instant,
    val toolingRequired: String?,
    val toolingProvided: String?,
    val location: LocationDto,
    val sallaryType: SallaryType,
    val sallaryAmount: String,
    val sallaryUnit: String?,
    val sallaryProductName: String?,
    val categories: List<String>?
)
