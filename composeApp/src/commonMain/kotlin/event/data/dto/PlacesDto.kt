package event.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponseDto(
    val items: List<PlaceDto>
)

@Serializable
data class PlaceDto(
    val mainText: String,
    val secondaryText: String,
    val placeId: String
)
