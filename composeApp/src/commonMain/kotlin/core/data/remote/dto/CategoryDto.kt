import kotlinx.serialization.Serializable
import ui.domain.ColorVariation

@Serializable
data class EventCategoryDto(
    val icon: String,
    val name: String,
    val colorVariant: ColorVariation
)