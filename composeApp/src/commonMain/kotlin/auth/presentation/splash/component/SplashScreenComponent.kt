package auth.presentation.splash.component

import auth.domain.model.AccountType
import auth.domain.use_case.VerifyTokenUseCase
import auth.presentation.splash.SplashState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import kotlinx.coroutines.launch

class SplashScreenComponent(
    componentContext: ComponentContext,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val onForkNavigateToApp: (valid: Boolean, error: String?) -> Unit,
    private val databaseClient: SqlDelightDatabaseClient
) : ComponentContext by componentContext {
    private val _stateSplash = MutableValue(SplashState(isLoading = false, error = null))
    val splashState: Value<SplashState> = _stateSplash

    fun verifyUserToken() {
        coroutineScope().launch {
            verifyTokenUseCase().collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        onForkNavigateToApp(true, null)
                    }

                    is ResultHandler.Error -> {
                        if (result.error === DataError.NetworkError.NO_INTERNET) {
                            try {
                                val user = databaseClient.selectUser()
                                val inProgressEvent = databaseClient.selectEvent()
                                if (user.accountType == AccountType.ORGANISER.toString()) {
                                    onForkNavigateToApp(true, result.error.asUiText().asNonCompString())
                                } else {
                                    onForkNavigateToApp(false, null)
                                }
                            } catch (e: Exception) {
                                println(e)
                                _stateSplash.value = _stateSplash.value.copy(
                                    error = result.error.asUiText().asNonCompString(),
                                )
                                onForkNavigateToApp(false, _stateSplash.value.error)
                            }

                        } else {
                            _stateSplash.value = _stateSplash.value.copy(
                                error = result.error.asUiText().asNonCompString()
                            )
                            onForkNavigateToApp(false, _stateSplash.value.error)
                        }

                    }

                    is ResultHandler.Loading -> {
                        _stateSplash.value = _stateSplash.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}