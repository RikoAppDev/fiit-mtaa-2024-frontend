package home_screen.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.grabit.User
import core.data.remote.dto.EventCardDto
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import home_screen.domain.use_case.GetLatestEventsUseCase
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val getLatestEventsUseCase: GetLatestEventsUseCase,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateToEventDetailScreen: (id: String) -> Unit,
    val user: User
) : ComponentContext by componentContext {

    private val _isLatestEventsLoading = MutableValue(true)
    val isLatestEventsLoading: Value<Boolean> = _isLatestEventsLoading

    private val _latestEvents = MutableValue<List<EventCardDto>>(emptyList())
    val latestEvents: Value<List<EventCardDto>> = _latestEvents

    private val _isLoading = MutableValue(false)
    val isLoading: Value<Boolean> = _isLoading

    private val _error = MutableValue("")
    val error: Value<String> = _error

    fun loadLatestEvents() {
        this@HomeScreenComponent.coroutineScope().launch {
            getLatestEventsUseCase().collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        _isLatestEventsLoading.value = false
                        _latestEvents.value = result.data.events
                        _error.value = ""
                    }

                    is ResultHandler.Error -> {
                        _error.value = result.error.asUiText().asNonCompString()
                        _isLatestEventsLoading.value = false
                    }

                    is ResultHandler.Loading -> {
                        _isLatestEventsLoading.value = true
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            is HomeScreenEvent.NavigateToEventDetailScreen -> {
                onNavigateToEventDetailScreen(event.id)
            }

            is HomeScreenEvent.NavigateBottomBarItem -> {
                onNavigateBottomBarItem(event.navigationEvent)
            }

            HomeScreenEvent.RemoveError -> {
                _error.value = ""
            }
        }
    }
}
