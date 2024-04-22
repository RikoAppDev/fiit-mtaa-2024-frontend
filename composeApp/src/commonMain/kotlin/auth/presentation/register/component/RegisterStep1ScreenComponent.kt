package auth.presentation.register.component

import auth.domain.AuthValidation
import auth.domain.model.AccountType
import auth.domain.model.NewUser
import auth.presentation.register.RegisterStep1State
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
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
            error = null,
        )
    )
    val stateRegisterStep1: Value<RegisterStep1State> = _stateRegisterStep1

    fun onEvent(event: RegisterStep1ScreenEvent) {
        when (event) {
            is RegisterStep1ScreenEvent.GoBackToLogin -> onNavigateBackToLoginScreen()
            is RegisterStep1ScreenEvent.ClickButtonNext -> {
                validateForm()
                if (isFormValid()) {
                    onNavigateToRegisterStep2Screen(
                        NewUser(
                            email = _stateRegisterStep1.value.email,
                            password = _stateRegisterStep1.value.password,
                            accountType = AccountType.HARVESTER,
                            name = "",
                            phoneNumber = null
                        )
                    )
                }
            }

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

            RegisterStep1ScreenEvent.RemoveError -> {
                _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                    error = null
                )
            }
        }
    }

    private fun isFormValid(): Boolean {
        return _stateRegisterStep1.value.email.isNotEmpty()
                && _stateRegisterStep1.value.password.isNotEmpty()
                && _stateRegisterStep1.value.passwordRepeated.isNotEmpty()
                && _stateRegisterStep1.value.error == null
    }

    private fun validateForm() {
        validateEmail()
        if (_stateRegisterStep1.value.error == null) {
            validatePassword()
            if (_stateRegisterStep1.value.error == null) {
                validatePasswordMatch()
            }
        }
    }

    private fun validateEmail() {
        coroutineScope().launch {
            authValidation.validateEmail(_stateRegisterStep1.value.email).collect { result ->
                if (result is ResultHandler.Error) {
                    _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                        error = result.error.asUiText().asNonCompString()
                    )
                } else if (result is ResultHandler.Success) {
                    _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                        error = null
                    )
                }
            }
        }
    }

    private fun validatePassword() {
        coroutineScope().launch {
            authValidation.validatePassword(_stateRegisterStep1.value.password).collect { result ->
                if (result is ResultHandler.Error) {
                    _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                        error = result.error.asUiText().asNonCompString()
                    )
                } else if (result is ResultHandler.Success) {
                    _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                        error = null
                    )
                }
            }
        }
    }

    private fun validatePasswordMatch() {
        coroutineScope().launch {
            authValidation.matchPasswords(
                _stateRegisterStep1.value.password,
                _stateRegisterStep1.value.passwordRepeated
            ).collect { result ->
                if (result is ResultHandler.Error) {
                    _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                        error = result.error.asUiText().asNonCompString()
                    )
                } else if (result is ResultHandler.Success) {
                    _stateRegisterStep1.value = _stateRegisterStep1.value.copy(
                        error = null
                    )
                }
            }
        }
    }
}