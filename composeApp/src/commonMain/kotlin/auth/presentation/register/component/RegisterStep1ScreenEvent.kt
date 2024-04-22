package auth.presentation.register.component

sealed interface RegisterStep1ScreenEvent {
    data object GoBackToLogin : RegisterStep1ScreenEvent
    data object ClickButtonNext : RegisterStep1ScreenEvent
    data class UpdateEmail(val email: String) : RegisterStep1ScreenEvent
    data class UpdatePassword(val password: String) : RegisterStep1ScreenEvent
    data class UpdatePasswordRepeated(val passwordRepeated: String) : RegisterStep1ScreenEvent
    data object RemoveError : RegisterStep1ScreenEvent
}