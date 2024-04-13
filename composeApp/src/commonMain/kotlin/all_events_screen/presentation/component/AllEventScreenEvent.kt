package all_events_screen.presentation.component

import core.domain.event.SallaryType
import navigation.BottomNavigationEvent

sealed interface AllEventScreenEvent {
    data class ApplyFilter(
        val categoryFilter: String?,
        val sallaryFilter: SallaryType?,
        val distanceFilter: Number?
    ) : AllEventScreenEvent

    data class EventDetailScreen(val eventId: String) : AllEventScreenEvent
    data class NavigateBottomBarItem(val navigationEvent: BottomNavigationEvent) :
        AllEventScreenEvent

    data object NavigateToAccountDetailScreen : AllEventScreenEvent
    data object RemoveError : AllEventScreenEvent
    data class OnLiveEventTagClick(val id: String) : AllEventScreenEvent
}