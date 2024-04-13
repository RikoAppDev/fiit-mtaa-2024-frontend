package core.data.remote.dto

import kotlinx.serialization.Serializable
import ui.domain.ColorVariation

@Serializable
data class EventCategoryDto(
    val icon: String,
    val name: String,
    val colorVariant: ColorVariation
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }
}