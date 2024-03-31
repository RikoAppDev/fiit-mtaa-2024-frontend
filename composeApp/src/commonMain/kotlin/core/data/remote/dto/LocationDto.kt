import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val address: String,
    val city: String,
    val locationLat: Double,
    val locationLon: Double,
    val name: String? = null
)