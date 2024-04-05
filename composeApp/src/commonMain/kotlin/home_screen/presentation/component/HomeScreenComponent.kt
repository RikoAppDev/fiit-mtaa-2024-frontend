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

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val getLatestEventsUseCase: GetLatestEventsUseCase,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateToEventDetailScreen: (id: String) -> Unit,
    val user:User
) : ComponentContext by componentContext {

    private val _isPopularEventsLoading = MutableValue(true)
    val isPopularEventsLoading: Value<Boolean> = _isPopularEventsLoading

    private val _latestEvents = MutableValue<List<EventCardDto>>(emptyList())
    val latestEvents: Value<List<EventCardDto>> = _latestEvents

    fun loadLatestEvents() {
        this@HomeScreenComponent.coroutineScope().launch {
            getLatestEventsUseCase().collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        println(result.data)
                        _isPopularEventsLoading.value = false
                        _latestEvents.value = result.data.events
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


    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            is HomeScreenEvent.NavigateToEventDetailScreen -> {
                onNavigateToEventDetailScreen(event.id)
            }
        }
    }

}