package auth.presentation.register.component

import auth.domain.AuthValidation
import auth.domain.model.AccountType
import auth.domain.model.NewUser
import auth.presentation.register.RegisterStep1State
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RegisterStep1ScreenComponent(
    componentContext: ComponentContext,
    private val authValidation: AuthValidation,
    email: String,
    private val onNavigateBackToLoginScreen: () -> Unit,
    private val onNavigateToRegisterStep2Screen: (newUser: NewUser) -> Unit
) : ComponentContext by componentContext {
    private val _stateRegisterStep1 = MutableValue(
        RegisterStep1State(
            email = email,
            password = "",
            passwordRepeated = "",
            error = null
        )
    )
    val stateRegisterStep1: Value<RegisterStep1State> = _stateRegisterStep1

    private val _passwordsMatch = MutableValue(false)
    val passwordsMatch: Value<Boolean> = _passwordsMatch

    private val _isValid = MutableValue(false)
    val isValid: Value<Boolean> = _isValid

    fun onEvent(event: RegisterStep1ScreenEvent) {
        when (event) {
            is RegisterStep1ScreenEvent.GoBackToLogin -> onNavigateBackToLoginScreen()
            is RegisterStep1ScreenEvent.ClickButtonNext -> onNavigateToRegisterStep2Screen(
                NewUser(
                    email = _stateRegisterStep1.value.email,
                    password = _stateRegisterStep1.value.password,
                    accountType = AccountType.HARVESTER,
                    name = "",
                    phoneNumber = null
                )
            )

            is RegisterStep1ScreenEvent.UpdateEmail -> {
                _stateRegisterStep1.value = _stateRegisterStep1.value.copy(email = event.email)
            }

            is RegisterStep1ScreenEvent.UpdatePassword -> {
                _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                    password = event.password
                )
            }

            is RegisterStep1ScreenEvent.UpdatePasswordRepeated -> {
                _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                    passwordRepeated = event.passwordRepeated
                )
            }
        }
    }

    private fun validateForm() {
//        authValidation.validateEmail(stateRegisterStep1.value.email)
    }
}