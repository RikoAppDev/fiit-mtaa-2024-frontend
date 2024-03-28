package account_detail.presentation.account_detail.component

import auth.domain.model.AccountType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class AccountDetailComponent(
    componentContext: ComponentContext,
        private val onNavigateBack: () -> Unit,
    ) :
    ComponentContext by componentContext {

    private val defaultName = "Your name"
    private val defaultPhoneNumber = "+421 915 071 126"
    val accountType = AccountType.HARVESTER

    private val _name = MutableValue(defaultName)
    val name: Value<String> = _name

    private val _phoneNumber = MutableValue(defaultPhoneNumber)
    val phoneNumber: Value<String> = _phoneNumber

    private val _isEditing = MutableValue(false)
    val isEditing: Value<Boolean> = _isEditing

    fun onEvent(event: AccountDetailScreenEvent) {
        when (event) {
            is AccountDetailScreenEvent.ChangeName -> {
                _name.value = event.name
            }

            is AccountDetailScreenEvent.ChangePhone -> {
                _phoneNumber.value = event.phoneNumber
            }

            is AccountDetailScreenEvent.SaveChanges -> {
                //call DB for update
            }

            is AccountDetailScreenEvent.UpdateAccount -> {
                _isEditing.value = true
            }

            is AccountDetailScreenEvent.DiscardChanges -> {
                _name.value = defaultName
                _phoneNumber.value = defaultPhoneNumber
                _isEditing.value = false
            }

            is AccountDetailScreenEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }
}

