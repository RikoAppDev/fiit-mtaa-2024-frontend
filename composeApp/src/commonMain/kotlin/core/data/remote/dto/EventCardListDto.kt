package core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventCardListDto (
    val events: List<EventCardDto>
)