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
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.account_detail__success
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

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

    private val _snackBarText = MutableValue("")
    val snackBarText: Value<String> = _snackBarText

    private val _isLoading = MutableValue(false)
    val isLoading: Value<Boolean> = _isLoading

    private val _error = MutableValue("")
    val error: Value<String> = _error

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

            is AccountDetailScreenEvent.RemoveSnackbarText -> {
                _snackBarText.value = ""
                _error.value = ""
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun updateUser(updateObject: UpdateUser, token: String) {
        this@AccountDetailComponent.coroutineScope().launch {
            updateUserUseCase(updateObject, token).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.updateUser(updateObject, email)
                        _isEditing.value = false
                        _isLoading.value = false
                        _snackBarText.value = getString(Res.string.account_detail__success)
                    }

                    is ResultHandler.Error -> {
                        _error.value = result.error.asUiText().asNonCompString()
                        _snackBarText.value = _error.value
                        _isLoading.value = false
                    }

                    is ResultHandler.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        }
    }
}

