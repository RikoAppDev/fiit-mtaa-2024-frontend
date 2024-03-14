package auth.presentation.register.component

sealed interface RegisterStep3ScreenEvent {
    data class UpdateName(val name: String) : RegisterStep3ScreenEvent
    data class UpdatePhone(val phone: String) : RegisterStep3ScreenEvent
    data object ClickButtonNext : RegisterStep3ScreenEvent
}