package auth.presentation.register.component

import auth.domain.model.AccountType
import auth.domain.model.NewUser
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class RegisterStep2ScreenComponent(
    componentContext: ComponentContext,
    private val newUser: NewUser,
    private val onNavigateToRegisterStep3Screen: (NewUser) -> Unit
) : ComponentContext by componentContext {
    private val _accountType = MutableValue(AccountType.HARVESTER)
    val accountType: Value<AccountType> = _accountType

    fun onEvent(event: RegisterStep2ScreenEvent) {
        when (event) {
            is RegisterStep2ScreenEvent.UpdateAccountType -> {
                _accountType.value = event.accountType
            }

            is RegisterStep2ScreenEvent.OnNextStepButtonClick -> onNavigateToRegisterStep3Screen(
                newUser.copy(accountType = _accountType.value)
            )
        }
    }
}