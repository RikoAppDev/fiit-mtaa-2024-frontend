package auth.presentation.login.component

import auth.data.remote.dto.toUser
import auth.domain.AuthValidation
import auth.domain.model.Login
import auth.domain.use_case.LoginUserUseCase
import auth.presentation.login.LoginState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import kotlinx.coroutines.launch

class LoginScreenComponent(
    componentContext: ComponentContext,
    private val loginUserUseCase: LoginUserUseCase,
    private val authValidation: AuthValidation,
    private val databaseClient: SqlDelightDatabaseClient,
    error: String?,
    private val onNavigateToApplication: () -> Unit,
    private val onNavigateToRegisterScreen: (String) -> Unit
) : ComponentContext by componentContext {
    private val _stateLogin = MutableValue(
        LoginState(
            isLoading = false,
            email = "",
            password = "",
            dataError = error,
            emailError = null,
            passwordError = null
        )
    )
    val stateLogin: Value<LoginState> = _stateLogin

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.ClickRegisterButton -> {
                onNavigateToRegisterScreen(_stateLogin.value.email)
            }

            is LoginScreenEvent.ClickLoginButton -> {
                println(_stateLogin.value)
//                validateEmail()
//                validatePassword()

                loginUser(
                    login = Login(
                        email = _stateLogin.value.email,
                        password = _stateLogin.value.password
                    )
                )
            }

            is LoginScreenEvent.UpdateEmail -> {
                _stateLogin.value = _stateLogin.value.copy(email = event.email)
            }

            is LoginScreenEvent.UpdatePassword -> {
                _stateLogin.value = _stateLogin.value.copy(password = event.password)
            }

            is LoginScreenEvent.RemoveError -> {
                _stateLogin.value = _stateLogin.value.copy(
                    dataError = null, emailError = null, passwordError = null
                )
            }
        }
    }

    private fun isFormValid(): Boolean {
        return _stateLogin.value.email.isNotEmpty() && _stateLogin.value.password.isNotEmpty() && _stateLogin.value.emailError != null && _stateLogin.value.passwordError != null
    }

    private fun validateEmail() {
        coroutineScope().launch {
            authValidation.validateEmail(_stateLogin.value.email).collect { result ->
                if (result is ResultHandler.Error) {
                    println("Email result: $result")
                    _stateLogin.value = _stateLogin.value.copy(
                        emailError = result.error.asUiText().asNonCompString()
                    )
                    println(result)
                    println(_stateLogin.value)
                } else if (result is ResultHandler.Success) {
                    _stateLogin.value = _stateLogin.value.copy(
                        emailError = null
                    )
                    println(_stateLogin.value)
                }
            }
        }
    }

    private fun validatePassword() {
        coroutineScope().launch {
            authValidation.validatePassword(_stateLogin.value.password).collect { result ->
                if (result is ResultHandler.Error) {
                    println("Password result: $result")
                    _stateLogin.value = _stateLogin.value.copy(
                        passwordError = result.error.asUiText().asNonCompString()
                    )
                    println(result)
                    println(_stateLogin.value)
                } else if (result is ResultHandler.Success) {
                    _stateLogin.value = _stateLogin.value.copy(
                        passwordError = null
                    )
                    println(_stateLogin.value)
                }
            }
        }
        println(_stateLogin.value)
    }

    private fun loginUser(login: Login) {
        coroutineScope().launch {
            loginUserUseCase(login).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.insertFullUser(
                            result.data.toUser()
                        )
                        onNavigateToApplication()
                    }

                    is ResultHandler.Error -> {
                        _stateLogin.value = _stateLogin.value.copy(
                            dataError = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateLogin.value = _stateLogin.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}