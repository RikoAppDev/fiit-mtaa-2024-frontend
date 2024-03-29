package core.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class EventsCardListDto (
    val events: List<EventCardDto>
)