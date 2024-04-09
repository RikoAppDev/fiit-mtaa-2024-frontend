package events_on_map_screen.data

import kotlinx.serialization.Serializable
@Serializable
data class PointListDto (
    val events: List<PointDto>
)