package all_events_screen.presentation.component

import all_events_screen.domain.use_case.LoadCategoriesWithCountUseCase
import all_events_screen.domain.use_case.LoadFilteredEventsUseCase
import all_events_screen.presentation.AllEventsState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.ResultHandler
import core.domain.event.SallaryType
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent


class AllEventScreenComponent(
    componentContext: ComponentContext,
    private val loadCategoriesWithCountUseCase: LoadCategoriesWithCountUseCase,
    private val loadFilteredEventsUseCase: LoadFilteredEventsUseCase,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val navigateToEventDetailScreen: (id: String) -> Unit
) : ComponentContext by componentContext {
    private val _allEventsState = MutableValue(
        AllEventsState(
            isLoadingCategories = true,
            isLoadingEvents = true,
            categories = null,
            events = null
        )
    )
    val allEventsState: Value<AllEventsState> = _allEventsState

    fun onEvent(event: AllEventScreenEvent) {
        when (event) {
            is AllEventScreenEvent.ApplyFilter -> {
                loadFilteredEvents(event.categoryFilter, event.sallaryFilter, event.distanceFilter)
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
                        //TODO - HANDLE ERROR
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
        filterDistance: Number?
    ) {
        this@AllEventScreenComponent.coroutineScope().launch {
            loadFilteredEventsUseCase(
                filterCategory,
                filterSallary,
                filterDistance
            ).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _allEventsState.value = _allEventsState.value.copy(
                            events = result.data,
                            isLoadingEvents = false
                        )
                    }

                    is ResultHandler.Error -> {
                        //TODO - HANDLE ERROR
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