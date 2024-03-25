package account_detail.presentation.account_detail.component

interface AccountDetailScreenEvent {
    data object UpdateAccount : AccountDetailScreenEvent
    data object SaveChanges : AccountDetailScreenEvent
}