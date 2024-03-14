package auth.presentation.register.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class RegisterStep1ScreenComponent(
    email: String,
    componentContext: ComponentContext,
    private val onNavigateBackToLoginScreen: () -> Unit,
    private val onNavigateToRegisterStep2Screen: () -> Unit
) : ComponentContext by componentContext {
    private val _email = MutableValue(email)
    val email: Value<String> = _email
    private val _password = MutableValue("")
    val password: Value<String> = _password
    private val _passwordRepeated = MutableValue("")
    val passwordRepeated: Value<String> = _passwordRepeated

    fun onEvent(event: RegisterStep1ScreenEvent) {
        when (event) {
            is RegisterStep1ScreenEvent.GoBackToLogin -> onNavigateBackToLoginScreen
            is RegisterStep1ScreenEvent.ClickButtonNext -> onNavigateToRegisterStep2Screen
            is RegisterStep1ScreenEvent.UpdateEmail -> {
                _email.value = event.email
            }

            is RegisterStep1ScreenEvent.UpdatePassword -> {
                _password.value = event.password
            }

            is RegisterStep1ScreenEvent.UpdatePasswordRepeated -> {
                _passwordRepeated.value = event.passwordRepeated
            }
        }
    }
}