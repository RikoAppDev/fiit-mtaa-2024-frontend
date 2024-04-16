package event.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventCreateUpdateRespDto(
    val eventId: String
)
