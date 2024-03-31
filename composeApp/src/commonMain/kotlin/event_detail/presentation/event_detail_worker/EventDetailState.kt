package event_detail.presentation.event_detail_worker

import event_detail.data.dto.EventDetailDto

data class EventDetailState(
    val isLoading: Boolean,
    val eventDetail: EventDetailDto?,
    val error: String?
)