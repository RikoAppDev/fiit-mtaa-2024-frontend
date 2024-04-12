package event.data.dto

import EventCategoryDto
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesDto(
    val categories: List<EventCategoryDto>
)
