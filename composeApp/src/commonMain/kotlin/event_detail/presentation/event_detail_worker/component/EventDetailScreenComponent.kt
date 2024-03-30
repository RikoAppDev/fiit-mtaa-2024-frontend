package event_detail.presentation.event_detail_worker.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import event_detail.domain.use_case.LoadEventDataUseCase
import event_detail.presentation.event_detail_worker.EventDetailState
import kotlinx.coroutines.launch

class EventDetailScreenComponent(
    componentContext: ComponentContext,
    networkClient: KtorClient,
    id: String,
    private val onNavigateBack: () -> Unit,
) : ComponentContext by componentContext {
    private val loadEventDataUseCase = LoadEventDataUseCase(networkClient, id)

    private val _stateEventDetail =
        MutableValue(EventDetailState(isLoading = false, eventDetail = null, error = null))
    val stateEventDetail: Value<EventDetailState> = _stateEventDetail

    fun onEvent(event: EventDetailScreenEvent) {
        when (event) {
            is EventDetailScreenEvent.SignInForEvent -> handleSignInForEvent() // TODO
            EventDetailScreenEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    private fun handleSignInForEvent() {
        print("Wocap")
    }

    fun loadEventData() {
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventDataUseCase().collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        println(result.data)
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoading = false,
                            eventDetail = result.data
                        )
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
                        _stateEventDetail.value = _stateEventDetail.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}