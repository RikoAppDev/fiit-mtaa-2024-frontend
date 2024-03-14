package auth.presentation.register.component

import auth.domain.model.AccountType

sealed interface RegisterStep2ScreenEvent {
    data class UpdateAccountType(val accountType: AccountType) : RegisterStep2ScreenEvent
    data object OnNextStepButtonClick : RegisterStep2ScreenEvent
}