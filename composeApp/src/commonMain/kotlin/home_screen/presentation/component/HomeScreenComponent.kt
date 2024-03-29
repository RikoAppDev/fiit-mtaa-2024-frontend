package home_screen.presentation.component

import auth.data.remote.dto.toUser
import auth.domain.model.NewUser
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import home_screen.domain.use_case.GetLatestEventsUseCase
import kotlinx.coroutines.launch


class HomeScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    networkClient: KtorClient,
) : ComponentContext by componentContext {

    private val getLatestEventsUseCase = GetLatestEventsUseCase(networkClient)

    private fun loadLatestEvents() {
        this@HomeScreenComponent.coroutineScope().launch {
            getLatestEventsUseCase().collect { result ->

                when (result) {
                    is ResultHandler.Success -> {
                        println(result.data)
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

            is HomeScreenEvent.OnMagicButtonClick -> {
                loadLatestEvents()
            }
        }
    }

}