package event_detail.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventWorkersDto (
    val workers: List<EventWorkerDto>
)