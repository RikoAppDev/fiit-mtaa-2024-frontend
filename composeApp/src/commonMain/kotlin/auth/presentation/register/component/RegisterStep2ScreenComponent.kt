package auth.presentation.register.component

import auth.domain.model.AccountType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class RegisterStep2ScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToRegisterStep3Screen: () -> Unit
) : ComponentContext by componentContext {
    private val _accountType = MutableValue(AccountType.WORKER)
    val accountType: Value<AccountType> = _accountType

    fun onEvent(event: RegisterStep2ScreenEvent) {
        when (event) {
            is RegisterStep2ScreenEvent.UpdateAccountType -> {
                _accountType.value = event.accountType
            }

            is RegisterStep2ScreenEvent.OnNextStepButtonClick -> onNavigateToRegisterStep3Screen
        }
    }
}