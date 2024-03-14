package auth.presentation.login.component

sealed interface LoginScreenEvent {
    data object ClickRegisterButton : LoginScreenEvent
    data object ClickLoginButton : LoginScreenEvent
    data class UpdateEmail(val email: String) : LoginScreenEvent
    data class UpdatePassword(val password: String) : LoginScreenEvent
}