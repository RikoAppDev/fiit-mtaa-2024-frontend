package event.data.dto

import core.data.remote.dto.EventCategoryDto
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesDto(
    val categories: List<EventCategoryDto>
)
