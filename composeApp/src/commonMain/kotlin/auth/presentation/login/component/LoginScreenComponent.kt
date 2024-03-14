package auth.presentation.login.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update

class LoginScreenComponent(
    componentContext: ComponentContext,
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
            is LoginScreenEvent.ClickLoginButton -> TODO()
            is LoginScreenEvent.UpdateEmail -> {
                _email.value = event.email
            }

            is LoginScreenEvent.UpdatePassword -> {
                _password.value = event.password
                passwordHidden.value = hidePassword(password)
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