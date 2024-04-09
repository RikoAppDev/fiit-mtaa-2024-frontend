package auth.presentation.register.component

sealed interface RegisterStep3ScreenEvent {
    data class UpdateName(val name: String) : RegisterStep3ScreenEvent
    data class UpdatePhone(val phone: String) : RegisterStep3ScreenEvent
    data object ClickCreateAccountButton : RegisterStep3ScreenEvent
    data object RemoveError : RegisterStep3ScreenEvent
}