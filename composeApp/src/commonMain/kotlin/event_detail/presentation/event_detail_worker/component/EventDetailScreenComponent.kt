package event_detail.presentation.event_detail_worker.component

import account_detail.domain.use_case.UpdateUserUseCase
import auth.domain.model.AccountType
import auth.presentation.login.component.LoginScreenEvent
import coil3.network.NetworkClient
import coil3.size.Dimension
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import event_detail.data.dto.EventDetailDto
import event_detail.domain.use_case.LoadEventDataUseCase
import kotlinx.coroutines.launch

class EventDetailScreenComponent(
    componentContext: ComponentContext,
    id: String,
    private val onNavigateBack: () -> Unit,
    val networkClient: KtorClient
) : ComponentContext by componentContext {

    private val _isLoading = MutableValue(true)
    val isLoading: Value<Boolean> = _isLoading

    private val loadEventDataUseCase = LoadEventDataUseCase(networkClient, id)



//    private val _eventData = MutableValue<EventDetailDto : Any>(null)
//    val eventData: Value<EventDetailDto?> = _eventData.value

    init {
        println("EventID: $id")
        loadEventData()
    }

    fun onEvent(event: EventDetailScreenEvent) {
        when (event) {
            is EventDetailScreenEvent.SignInForEvent -> handleSignInForEvent() // TODO
            EventDetailScreenEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    fun handleSignInForEvent() {
        print("Wocap")
    }

    private fun loadEventData() {
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventDataUseCase().collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        println(result.data)
                        _isLoading.value = false
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

}