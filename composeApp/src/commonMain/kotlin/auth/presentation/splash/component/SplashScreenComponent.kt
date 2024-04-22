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
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.resources.desc.desc
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.biometry_request_reason
import grabit.composeapp.generated.resources.biometry_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

class SplashScreenComponent(
    componentContext: ComponentContext,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val onForkNavigateToApp: (valid: Boolean, error: String?) -> Unit,
    private val databaseClient: SqlDelightDatabaseClient
) : ComponentContext by componentContext {
    private val _stateSplash =
        MutableValue(SplashState(isLoading = false, error = null))
    val splashState: Value<SplashState> = _stateSplash

    @OptIn(ExperimentalResourceApi::class)
    private fun tryToAuth(biometryAuthenticator: BiometryAuthenticator) = coroutineScope().launch {
        try {
            val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                requestTitle = getString(Res.string.biometry_title).desc(),
                requestReason = getString(Res.string.biometry_request_reason).desc(),
                failureButtonText = "Oops".desc(),
                allowDeviceCredentials = true
            )

            if (isSuccess) {
                onForkNavigateToApp(true, null)
            } else {
                onForkNavigateToApp(false, null)
            }
        } catch (throwable: Throwable) {
            onForkNavigateToApp(false, null)
        }
    }

    fun verifyUserToken(
        biometryAuthenticator: BiometryAuthenticator,
        biometricAvailable: Boolean
    ) {
        coroutineScope().launch {
            verifyTokenUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        if (biometricAvailable) {
                            _stateSplash.value = _stateSplash.value.copy(
                                isLoading = false
                            )
                            tryToAuth(biometryAuthenticator)
                        } else {
                            onForkNavigateToApp(true, null)
                        }
                    }

                    is ResultHandler.Error -> {
                        if (result.error === DataError.NetworkError.NO_INTERNET) {
                            try {
                                val user = databaseClient.selectUser()
                                val inProgressEvent = databaseClient.selectEvent()
                                if (user.accountType == AccountType.ORGANISER.toString()) {
                                    onForkNavigateToApp(
                                        true,
                                        result.error.asUiText().asNonCompString()
                                    )
                                } else {
                                    onForkNavigateToApp(false, null)
                                }
                            } catch (e: Exception) {
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