package events_on_map_screen.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import core.domain.EventMarker
import navigation.BottomNavigationEvent

class EventsOnMapScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit
) : ComponentContext by componentContext {

    private val _markers = MutableValue(
        listOf(
            EventMarker("13", 48.946427, 18.118392),
            EventMarker("133", 48.746427, 18.218392),
            EventMarker("133", 48.846427, 18.318392),
        )
    )
    val markers: Value<List<EventMarker>> = _markers

    fun onEvent(event: EventsOnMapScreenEvent) {
        when (event) {
            is EventsOnMapScreenEvent.NavigateBottomBarItem -> {
                onNavigateBottomBarItem(event.navigationEvent)
            }

            EventsOnMapScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }
        }
    }
}