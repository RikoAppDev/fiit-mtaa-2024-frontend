package account_detail.presentation.account_detail.component

import account_detail.domain.model.UpdateUser
import account_detail.domain.use_case.LogOutUseCase
import account_detail.domain.use_case.UpdateUserUseCase
import androidx.compose.material.SnackbarHostState
import auth.data.remote.dto.toUser
import auth.domain.model.AccountType
import auth.domain.model.Login
import auth.domain.model.NewUser
import auth.domain.use_case.LoginUserUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import kotlinx.coroutines.launch

class AccountDetailComponent(
    componentContext: ComponentContext,
    val databaseClient: SqlDelightDatabaseClient,
    val networkClient: KtorClient,
    private val onNavigateBack: () -> Unit,
    private val onNavigateToLoginScreen: () -> Unit
) :
    ComponentContext by componentContext {

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

    private val updateUserUseCase = UpdateUserUseCase(networkClient)

    private fun showToast(message:String){
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

            is AccountDetailScreenEvent.LogOut ->{
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
                        println("Update success!!!")
                        databaseClient.updateUser(updateObject, email)
                        _isEditing.value = false
                    }

                    is ResultHandler.Error -> {
                        when (result.error) {
                            DataError.NetworkError.REDIRECT -> println(DataError.NetworkError.REDIRECT.name)
                            DataError.NetworkError.BAD_REQUEST -> println(DataError.NetworkError.BAD_REQUEST.name)
                            DataError.NetworkError.REQUEST_TIMEOUT -> println(DataError.NetworkError.REQUEST_TIMEOUT.name)
                            DataError.NetworkError.TOO_MANY_REQUESTS -> println(DataError.NetworkError.TOO_MANY_REQUESTS.name)
                            DataError.NetworkError.NO_INTERNET -> println(DataError.NetworkError.NO_INTERNET.name)
                            DataError.NetworkError.PAYLOAD_TOO_LARGE -> println(DataError.NetworkError.PAYLOAD_TOO_LARGE.name)
                            DataError.NetworkError.SERVER_ERROR -> println(DataError.NetworkError.SERVER_ERROR.name)
                            DataError.NetworkError.SERIALIZATION -> println(DataError.NetworkError.SERIALIZATION.name)
                            DataError.NetworkError.UNKNOWN -> println(DataError.NetworkError.UNKNOWN.name)
                        }
                    }

                    is ResultHandler.Loading -> {
                        println("loading")
                    }
                }
            }
        }
    }
}

