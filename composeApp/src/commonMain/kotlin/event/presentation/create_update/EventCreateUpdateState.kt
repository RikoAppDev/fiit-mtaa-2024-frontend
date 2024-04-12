package event.presentation.create_update

import event.data.dto.EventCreateUpdateDto

data class EventCreateUpdateState(
    val isLoading: Boolean,
    val event: EventCreateUpdateDto?,
    val error: String?
)
