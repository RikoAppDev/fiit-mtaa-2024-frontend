package event.presentation.create_update

import event.domain.model.Event

data class EventCreateUpdateState(
    val isLoading: Boolean,
    val event: Event?,
    val error: String?
)
