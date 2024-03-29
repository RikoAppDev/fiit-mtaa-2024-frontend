package home_screen.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.remote.KtorClient
import core.data.remote.dto.EventCardDto
import core.domain.DataError
import core.domain.ResultHandler
import home_screen.domain.use_case.GetLatestEventsUseCase
import kotlinx.coroutines.launch


class HomeScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateToEventDetailScreen: (id:String) -> Unit,
    networkClient: KtorClient,
) : ComponentContext by componentContext {

    private val _isPopularEventsLoading = MutableValue(true)
    val isPopularEventsLoading: Value<Boolean> = _isPopularEventsLoading

    private val _latestEvents = MutableValue<List<EventCardDto>>(emptyList())
    val latestEvents: Value<List<EventCardDto>> = _latestEvents

    private val getLatestEventsUseCase = GetLatestEventsUseCase(networkClient)

    init {
        loadLatestEvents()
    }
    private fun loadLatestEvents() {
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
                        when (result.error) {
                            DataError.NetworkError.REDIRECT -> println(DataError.NetworkError.REDIRECT.name)
                            DataError.NetworkError.BAD_REQUEST -> println(DataError.NetworkError.BAD_REQUEST.name)
                            DataError.NetworkError.REQUEST_TIMEOUT -> println(DataError.NetworkError.REQUEST_TIMEOUT.name)
                            DataError.NetworkError.TOO_MANY_REQUESTS -> println(DataError.NetworkError.TOO_MANY_REQUESTS.name)
                            DataError.NetworkError.NO_INTERNET -> println(DataError.NetworkError.NO_INTERNET.name)
                            DataError.NetworkError.PAYLOAD_TOO_LARGE -> println(DataError.NetworkError.PAYLOAD_TOO_LARGE.name)
                            DataError.NetworkError.SERVER_ERROR -> println(DataError.NetworkError.SERVER_ERROR.name)
                            DataError.NetworkError.SERIALIZATION -> println(DataError.NetworkError.SERIALIZATION.name)
                            DataError.NetworkError.UNKNOWN -> println(DataError.NetworkError.UNKNOWN.name)
                        }
                    }

                    is ResultHandler.Loading -> {
                        println("loading")
                    }
                }
            }
        }
    }


    fun onEvent(event:HomeScreenEvent){
        when(event){
            HomeScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            is HomeScreenEvent.NavigateToEventDetailScreen -> {
                onNavigateToEventDetailScreen(event.id)
            }
        }
    }

}