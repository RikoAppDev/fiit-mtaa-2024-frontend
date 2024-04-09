package home_screen.presentation.component

import navigation.BottomNavigationEvent

sealed interface HomeScreenEvent {
    data object NavigateToAccountDetailScreen : HomeScreenEvent
    data class NavigateToEventDetailScreen(val id: String) : HomeScreenEvent
    data class NavigateBottomBarItem(val navigationEvent: BottomNavigationEvent) : HomeScreenEvent
    data object RemoveError : HomeScreenEvent

    data class NavigateToActiveEvent(val id: String) : HomeScreenEvent
}