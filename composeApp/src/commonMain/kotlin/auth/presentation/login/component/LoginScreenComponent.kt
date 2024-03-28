package auth.presentation.login.component

import auth.data.remote.dto.toUser
import auth.domain.model.Login
import auth.domain.use_case.LoginUserUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import kotlinx.coroutines.launch

class LoginScreenComponent(
    componentContext: ComponentContext,
    networkClient: KtorClient,
    private val databaseClient: SqlDelightDatabaseClient,
    private val onNavigateToApplication: () -> Unit,
    private val onNavigateToRegisterScreen: (String) -> Unit
) : ComponentContext by componentContext {
    private val loginUserUseCase = LoginUserUseCase(networkClient)

    private val _email = MutableValue("")
    val email: Value<String> = _email
    private val _password = MutableValue("")
    val password: Value<String> = _password
    val passwordHidden = MutableValue("")

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.ClickRegisterButton -> onNavigateToRegisterScreen(email.value)

            is LoginScreenEvent.ClickLoginButton -> {
                loginUser(login = Login(email = _email.value, password = _password.value))
            }

            is LoginScreenEvent.UpdateEmail -> {
                _email.value = event.email
            }

            is LoginScreenEvent.UpdatePassword -> {
                _password.value = event.password
                passwordHidden.value = hidePassword(password)
            }
        }
    }

    private fun loginUser(login: Login) {
        this@LoginScreenComponent.coroutineScope().launch {
            loginUserUseCase(login).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.insertFullUser(
                            result.data.toUser()
                        )
                        onNavigateToApplication()
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


    private fun updatePassword(password: String, length: Int) {
        if (_password.value.length < length / 2) {
            _password.value += password.last().toString()
        } else {
            _password.value.dropLast(2)
            passwordHidden.value.dropLast(2)
        }
    }

    private fun hidePassword(password: Value<String>): String {
        val replacementString = ".*"

        val modifiedString = buildString {
            repeat(password.value.length) {
                append(replacementString)
            }
        }

        return modifiedString
    }
}