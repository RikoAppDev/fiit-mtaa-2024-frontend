package auth.presentation.register.component

import auth.data.remote.dto.toUser
import auth.domain.AuthValidation
import auth.domain.model.NewUser
import auth.domain.use_case.RegisterUserUseCase
import auth.presentation.register.RegisterStep3State
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.decompose.value.Value
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import core.domain.DataError.NetworkError.*
import core.domain.NetworkHandler
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import kotlinx.coroutines.launch

class RegisterStep3ScreenComponent(
    componentContext: ComponentContext,
    private val registerUserUseCase: RegisterUserUseCase,
    newUser: NewUser,
    private val databaseClient: SqlDelightDatabaseClient,
    private val onNavigateToRegisterStepFinalScreen: () -> Unit
) : ComponentContext by componentContext {

    private val _stateRegisterStep3State = MutableValue(
        RegisterStep3State(
            isLoading = false,
            newUser = newUser,
            name = "",
            phoneNumber = "",
            error = null
        )
    )
    val stateRegisterStep3: Value<RegisterStep3State> = _stateRegisterStep3State

    fun onEvent(event: RegisterStep3ScreenEvent) {
        when (event) {
            is RegisterStep3ScreenEvent.UpdateName -> {
                _stateRegisterStep3State.value = _stateRegisterStep3State.value.copy(
                    name = event.name
                )
            }

            is RegisterStep3ScreenEvent.UpdatePhone -> {
                _stateRegisterStep3State.value = _stateRegisterStep3State.value.copy(
                    phoneNumber = event.phone
                )
            }

            is RegisterStep3ScreenEvent.ClickCreateAccountButton -> {
                createAccount(
                    newUser = _stateRegisterStep3State.value.newUser.copy(
                        name = _stateRegisterStep3State.value.name,
                        phoneNumber = _stateRegisterStep3State.value.phoneNumber
                    )
                )
            }
        }
    }

    private fun createAccount(newUser: NewUser) {
        this@RegisterStep3ScreenComponent.coroutineScope().launch {
            registerUserUseCase(newUser).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.insertFullUser(
                            result.data.toUser()
                        )
                        onNavigateToRegisterStepFinalScreen()
                    }

                    is ResultHandler.Error -> {
                        result.error.asUiText().asNonCompString()
                        println(result)
                    }

                    is ResultHandler.Loading -> {
                        _stateRegisterStep3State.value = _stateRegisterStep3State.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}