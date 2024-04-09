package events_on_map_screen.presentation.component

import navigation.BottomNavigationEvent

sealed interface EventsOnMapScreenEvent {
    data object NavigateToAccountDetailScreen : EventsOnMapScreenEvent

    data class OnEventOnMapClick(val id:String) : EventsOnMapScreenEvent

    data class NavigateToEventDetail(val id:String) : EventsOnMapScreenEvent

    data class NavigateBottomBarItem(val navigationEvent: BottomNavigationEvent) :
        EventsOnMapScreenEvent
}