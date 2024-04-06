package all_events_screen.data


import kotlinx.serialization.*

@Serializable
data class CategoriesWithCountDto(
    val categories: List<CategoryWithCountDto>
)