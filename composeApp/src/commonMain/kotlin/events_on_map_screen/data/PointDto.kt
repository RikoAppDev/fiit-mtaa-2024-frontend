package events_on_map_screen.data
import kotlinx.serialization.*


@Serializable
data class PointDto (
    val id: String,

    @SerialName("Location")
    val location: Location
)

@Serializable
data class Location (
    val locationLat: Double,
    val locationLon: Double
)