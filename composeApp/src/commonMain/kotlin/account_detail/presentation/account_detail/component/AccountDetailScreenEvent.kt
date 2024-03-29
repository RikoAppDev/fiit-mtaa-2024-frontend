package account_detail.presentation.account_detail.component

sealed interface AccountDetailScreenEvent {
    data class ChangeName(val name: String) : AccountDetailScreenEvent
    data class ChangePhone(val phoneNumber: String) : AccountDetailScreenEvent
    data object UpdateAccount : AccountDetailScreenEvent
    data object SaveChanges : AccountDetailScreenEvent
    data object DiscardChanges : AccountDetailScreenEvent
    data object NavigateBack: AccountDetailScreenEvent
    data object LogOut: AccountDetailScreenEvent
}