package navigation

sealed interface BottomNavigationEvent {
    data object OnNavigateToHomeScreen : BottomNavigationEvent
    data object OnNavigateToAllHarvestsScreen : BottomNavigationEvent
    data object OnNavigateToMapScreen : BottomNavigationEvent
    data object OnNavigateToMyHarvestScreen : BottomNavigationEvent
}