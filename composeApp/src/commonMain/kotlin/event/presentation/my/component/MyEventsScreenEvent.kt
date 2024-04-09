package event.presentation.my.component

import all_events_screen.presentation.component.AllEventScreenEvent
import navigation.BottomNavigationEvent

sealed interface MyEventsScreenEvent {
    data object NavigateToAccountDetailScreen : MyEventsScreenEvent


    data class NavigateToEventDetail(val id: String) : MyEventsScreenEvent

    data class NavigateBottomBarItem(val navigationEvent: BottomNavigationEvent) :
        MyEventsScreenEvent

    data object RemoveError : MyEventsScreenEvent
}