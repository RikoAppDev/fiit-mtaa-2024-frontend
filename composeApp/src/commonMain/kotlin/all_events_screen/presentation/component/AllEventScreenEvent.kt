package all_events_screen.presentation.component

import core.domain.event.SallaryType

sealed interface AllEventScreenEvent {
    data class ApplyFilter(
        val categoryFilter: String?,
        val sallaryFilter: SallaryType?,
        val distanceFilter: Number?
    ) : AllEventScreenEvent

    data class EventDetailScreen(val eventId: String) : AllEventScreenEvent


}