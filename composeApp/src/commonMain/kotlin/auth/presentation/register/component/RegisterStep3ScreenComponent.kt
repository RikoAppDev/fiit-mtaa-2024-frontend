package auth.presentation.register.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class RegisterStep3ScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToRegisterStepFinalScreen: () -> Unit
) : ComponentContext by componentContext {
    private val _name = MutableValue("")
    val name: Value<String> = _name
    private val _phone = MutableValue("")
    val phone: Value<String> = _phone

    fun onEvent(event: RegisterStep3ScreenEvent) {
        when (event) {
            is RegisterStep3ScreenEvent.UpdateName -> {
                _name.value = event.name
            }

            is RegisterStep3ScreenEvent.UpdatePhone -> {
                _phone.value = event.phone
            }

            is RegisterStep3ScreenEvent.ClickButtonNext -> onNavigateToRegisterStepFinalScreen
        }
    }
}