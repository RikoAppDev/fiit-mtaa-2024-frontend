package account_detail.presentation.account_detail.component

import account_detail.domain.model.UpdateUser
import account_detail.domain.use_case.LogOutUseCase
import account_detail.domain.use_case.UpdateUserUseCase
import androidx.compose.material.SnackbarHostState
import auth.domain.model.AccountType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import kotlinx.coroutines.launch

class AccountDetailComponent(
    componentContext: ComponentContext,
    private val updateUserUseCase: UpdateUserUseCase,
    private val databaseClient: SqlDelightDatabaseClient,
    private val onNavigateBack: () -> Unit,
    private val onNavigateToLoginScreen: () -> Unit
) : ComponentContext by componentContext {

    private var defaultName = ""
    private var defaultPhoneNumber = ""
    private var email = ""

    val accountType = AccountType.HARVESTER

    private val _name = MutableValue("")
    val name: Value<String> = _name

    private val _phoneNumber = MutableValue("")
    val phoneNumber: Value<String> = _phoneNumber

    private val _isEditing = MutableValue(false)
    val isEditing: Value<Boolean> = _isEditing

    private val _snackbarHostState = MutableValue(SnackbarHostState())
    val snackbarHostState: Value<SnackbarHostState> = _snackbarHostState

    private val logOutUseCase = LogOutUseCase(databaseClient)

    init {
        val user = databaseClient.selectUser()
        defaultName = user.name
        email = user.email
        defaultPhoneNumber = if (user.phoneNumber !== null) user.phoneNumber else ""
        _name.value = defaultName
        _phoneNumber.value = defaultPhoneNumber
    }


    private fun showToast(message: String) {
        this@AccountDetailComponent.coroutineScope().launch {
            snackbarHostState.value.showSnackbar(message)
        }
    }

    fun onEvent(event: AccountDetailScreenEvent) {
        when (event) {
            is AccountDetailScreenEvent.ChangeName -> {
                _name.value = event.name
            }

            is AccountDetailScreenEvent.ChangePhone -> {
                _phoneNumber.value = event.phoneNumber
            }

            is AccountDetailScreenEvent.SaveChanges -> {
                val token = databaseClient.selectUserToken()
                updateUser(
                    UpdateUser(
                        name = _name.value,
                        phoneNumber = _phoneNumber.value
                    ), token
                )
            }

            is AccountDetailScreenEvent.UpdateAccount -> {
                showToast("Wocap, toto je text")
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

            is AccountDetailScreenEvent.LogOut -> {
                logOutUseCase()
                onNavigateToLoginScreen()
            }
        }

    }

    private fun updateUser(updateObject: UpdateUser, token: String) {
        this@AccountDetailComponent.coroutineScope().launch {
            updateUserUseCase(updateObject, token).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.updateUser(updateObject, email)
                        _isEditing.value = false
                    }

                    is ResultHandler.Error -> {
                        result.error.asUiText().asNonCompString()
                    }

                    is ResultHandler.Loading -> {
                        println("loading")
                    }
                }
            }
        }
    }
}

