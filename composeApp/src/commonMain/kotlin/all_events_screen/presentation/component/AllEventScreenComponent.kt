package all_events_screen.presentation.component

import all_events_screen.domain.use_case.LoadCategoriesWithCountUseCase
import all_events_screen.domain.use_case.LoadFilteredEventsUseCase
import all_events_screen.presentation.AllEventsState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.GpsPosition
import core.domain.ResultHandler
import core.domain.event.SallaryType
import core.presentation.error_string_mapper.asUiText
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent


class AllEventScreenComponent(
    componentContext: ComponentContext,
    private val loadCategoriesWithCountUseCase: LoadCategoriesWithCountUseCase,
    private val loadFilteredEventsUseCase: LoadFilteredEventsUseCase,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val navigateToEventDetailScreen: (id: String) -> Unit,
    private val onNavigateToLiveEvent: (id: String) -> Unit
) : ComponentContext by componentContext {
    private val _actualLocation = MutableValue(GpsPosition(null, null))
    val actualLocation: Value<GpsPosition> = _actualLocation

    private val _allEventsState = MutableValue(
        AllEventsState(
            isLoadingCategories = true,
            isLoadingEvents = true,
            errorCategories = null,
            errorEvents = null,
            categories = null,
            events = null,
        )
    )
    val allEventsState: Value<AllEventsState> = _allEventsState

    fun onEvent(event: AllEventScreenEvent) {
        when (event) {
            is AllEventScreenEvent.ApplyFilter -> {
                loadFilteredEvents(
                    event.categoryFilter,
                    event.sallaryFilter,
                    event.distanceFilter,
                    event.actualLocation
                )
            }

            is AllEventScreenEvent.EventDetailScreen -> {
                navigateToEventDetailScreen(event.eventId)
            }

            is AllEventScreenEvent.NavigateBottomBarItem -> {
                onNavigateBottomBarItem(event.navigationEvent)
            }

            AllEventScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            AllEventScreenEvent.RemoveError -> {
                _allEventsState.value = _allEventsState.value.copy(
                    errorCategories = null, errorEvents = null
                )
            }

            is AllEventScreenEvent.OnLiveEventTagClick -> {
                onNavigateToLiveEvent(event.id)
            }
        }
    }

    fun initLocationTracker(locationTracker: LocationTracker) {
        locationTracker.getLocationsFlow().onEach {
            try {
                _actualLocation.value = GpsPosition(it.latitude, it.longitude)
            } catch (e: Exception) {
                println("Error: $e")
            }
            println("Location: ${actualLocation.value.latitude}, ${actualLocation.value.longitude}")
        }.launchIn(coroutineScope())
    }

    fun startLocationTracking(locationTracker: LocationTracker) {
        coroutineScope().launch {
            try {
                locationTracker.startTracking()
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }

    fun loadCategoriesWithCount() {
        this@AllEventScreenComponent.coroutineScope().launch {
            loadCategoriesWithCountUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            isLoadingCategories = false,
                            categories = result.data
                        )
                    }

                    is ResultHandler.Error -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            errorCategories = result.error.asUiText().asNonCompString(),
                            isLoadingCategories = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            isLoadingCategories = true
                        )
                    }
                }
            }
        }
    }

    fun loadFilteredEvents(
        filterCategory: String?,
        filterSallary: SallaryType?,
        filterDistance: Number?,
        actualLocation: GpsPosition
    ) {
        this@AllEventScreenComponent.coroutineScope().launch {
            loadFilteredEventsUseCase(
                filterCategory,
                filterSallary,
                filterDistance,
                actualLocation
            ).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            events = result.data,
                            isLoadingEvents = false
                        )
                    }

                    is ResultHandler.Error -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            errorEvents = result.error.asUiText().asNonCompString(),
                            isLoadingEvents = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            isLoadingEvents = true
                        )
                    }
                }
            }
        }
    }
}