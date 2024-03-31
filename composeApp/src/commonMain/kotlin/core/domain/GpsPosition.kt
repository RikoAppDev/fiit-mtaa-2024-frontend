package core.domain

import kotlinx.serialization.Serializable

@Serializable
class EventMarker(
    val eventId:String?,
    val latitude: Double,
    val longitude: Double
)

class GpsPosition(
    val latitude: Double,
    val longitude: Double
)