package auth.presentation.login.component

import auth.domain.AuthValidation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import core.domain.Error
import core.domain.ResultHandler

class LoginScreenComponent(
    componentContext: ComponentContext,
    private val authValidation: AuthValidation = AuthValidation(),
    private val onNavigateToRegisterScreen: (String) -> Unit
) : ComponentContext by componentContext {
    private val _email = MutableValue("")
    val email: Value<String> = _email
    private val _password = MutableValue("")
    val password: Value<String> = _password
    val passwordHidden = MutableValue("")

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.ClickRegisterButton -> onNavigateToRegisterScreen(email.value)
            is LoginScreenEvent.ClickLoginButton -> {
                onLoginClick(_email.value, _password.value)
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

    fun onLoginClick(email: String, password: String) {
        when (val resultHandler = authValidation.validateEmail(email)) {
            is ResultHandler.Error -> {
                when (resultHandler.error) {
                    AuthValidation.EmailError.INVALID_FORMAT -> TODO()
                }
            }

            is ResultHandler.Loading -> TODO()
            is ResultHandler.Success -> TODO()
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