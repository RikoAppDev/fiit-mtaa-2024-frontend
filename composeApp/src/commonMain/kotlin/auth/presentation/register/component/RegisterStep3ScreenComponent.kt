package auth.presentation.register.component

import auth.data.remote.dto.toUser
import auth.domain.model.NewUser
import auth.domain.use_case.RegisterUserUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.decompose.value.Value
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import core.domain.DataError.NetworkError.*
import core.domain.ResultHandler
import kotlinx.coroutines.launch

class RegisterStep3ScreenComponent(
    var newUser: NewUser,
    componentContext: ComponentContext,
    networkClient: KtorClient,
    private val databaseClient: SqlDelightDatabaseClient,
    private val onNavigateToRegisterStepFinalScreen: () -> Unit
) : ComponentContext by componentContext {
    private val registerUserUseCase = RegisterUserUseCase(networkClient)

    private val _name = MutableValue("")
    val name: Value<String> = _name
    private val _phone = MutableValue("")
    val phone: Value<String> = _phone

    fun onEvent(event: RegisterStep3ScreenEvent) {
        when (event) {
            is RegisterStep3ScreenEvent.UpdateName -> {
                _name.value = event.name
            }

            is RegisterStep3ScreenEvent.UpdatePhone -> {
                _phone.value = event.phone
            }

            is RegisterStep3ScreenEvent.ClickCreateAccountButton -> {
                createAccount(newUser.copy(name = _name.value, phoneNumber = _phone.value))
            }
        }
    }

    private fun createAccount(newUser: NewUser) {
        this@RegisterStep3ScreenComponent.coroutineScope().launch {
            registerUserUseCase(newUser).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.insertFullUser(
                            result.data.toUser()
                        )
                        onNavigateToRegisterStepFinalScreen()
                    }

                    is ResultHandler.Error -> {
                        when (result.error) {
                            REDIRECT -> println(REDIRECT.name)
                            BAD_REQUEST -> println(BAD_REQUEST.name)
                            REQUEST_TIMEOUT -> println(REQUEST_TIMEOUT.name)
                            TOO_MANY_REQUESTS -> println(TOO_MANY_REQUESTS.name)
                            NO_INTERNET -> println(NO_INTERNET.name)
                            PAYLOAD_TOO_LARGE -> println(PAYLOAD_TOO_LARGE.name)
                            SERVER_ERROR -> println(SERVER_ERROR.name)
                            SERIALIZATION -> println(SERIALIZATION.name)
                            UNKNOWN -> println(UNKNOWN.name)
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