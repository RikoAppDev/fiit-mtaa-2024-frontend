package home_screen.presentation.component

sealed interface HomeScreenEvent {
    data object NavigateToAccountDetailScreen : HomeScreenEvent
}