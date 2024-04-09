package events_on_map_screen.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.EventMarker
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.use_case.LoadEventDataUseCase
import events_on_map_screen.domain.use_case.LoadPointsUseCase
import events_on_map_screen.presentation.EventsOnMapState
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent

class EventsOnMapScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val getMapPointsUseCase: LoadPointsUseCase,
    private val loadEventDataUseCase: LoadEventDataUseCase,
    private val navigateToEventDetailScreen: (id: String) -> Unit
) : ComponentContext by componentContext {


    private val _eventsOnMapState = MutableValue(
        EventsOnMapState(
            isLoadingPoints = true,
            isLoadingEventSneakPeak = false,
            points = null,
            eventSneakPeak = null,
        )
    )
    val eventsOnMapState: Value<EventsOnMapState> = _eventsOnMapState

    fun onEvent(event: EventsOnMapScreenEvent) {
        when (event) {
            is EventsOnMapScreenEvent.NavigateBottomBarItem -> {
                onNavigateBottomBarItem(event.navigationEvent)
            }

            EventsOnMapScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            is EventsOnMapScreenEvent.OnEventOnMapClick -> {
                _eventsOnMapState.value = _eventsOnMapState.value.copy(
                    isLoadingEventSneakPeak = true,
                    eventSneakPeak = null,
                )
                getEventData(event.id)
            }

            is EventsOnMapScreenEvent.NavigateToEventDetail -> {
                navigateToEventDetailScreen(event.id)
            }
        }
    }

    fun getPoints() {
        this@EventsOnMapScreenComponent.coroutineScope().launch {
            getMapPointsUseCase().collect { result ->

                when (result) {
                    is ResultHandler.Success -> {
                        val events = result.data.events.map {
                            EventMarker(
                                it.id,
                                it.location.locationLat,
                                it.location.locationLon
                            )
                        }
                        _eventsOnMapState.value = _eventsOnMapState.value.copy(
                            isLoadingPoints = false,
                            points = events
                        )
                    }

                    is ResultHandler.Error -> {
                        result.error.asUiText().asNonCompString()
                    }

                    is ResultHandler.Loading -> {
                        println("loading")
                    }
                }
            }
        }
    }

    fun getEventData(id: String) {
        this@EventsOnMapScreenComponent.coroutineScope().launch {
            loadEventDataUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _eventsOnMapState.value = _eventsOnMapState.value.copy(
                            isLoadingEventSneakPeak = false,
                            eventSneakPeak = result.data
                        )
                    }

                    is ResultHandler.Error -> {
                        result.error.asUiText().asNonCompString()
                    }

                    is ResultHandler.Loading -> {
                        println("loading")
                    }
                }
            }
        }
    }
}