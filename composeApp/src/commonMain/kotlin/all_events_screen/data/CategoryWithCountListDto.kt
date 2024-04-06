package all_events_screen.data
import kotlinx.serialization.*
import ui.domain.ColorVariation

@Serializable
data class CategoryWithCountDto (
    @SerialName("_count")
    val count: Count,

    val colorVariant: ColorVariation,
    val icon: String,
    val id: String,
    val name: String
)

@Serializable
data class Count (
    @SerialName("EventCategoryRelation")
    val eventCategoryRelation: Long
)