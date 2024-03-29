package auth.presentation.splash.component

import auth.domain.AuthError
import auth.domain.use_case.VerifyTokenUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import kotlinx.coroutines.launch

class SplashScreenComponent(
    componentContext: ComponentContext,
    networkClient: KtorClient,
    private val databaseClient: SqlDelightDatabaseClient,
    private val onForkNavigateToApp: (Boolean) -> Unit
) : ComponentContext by componentContext {
    private val verifyTokenUseCase = VerifyTokenUseCase(networkClient)

    fun verifyUserToken() {
        this@SplashScreenComponent.coroutineScope().launch {
            try {
                val token = databaseClient.selectUserToken()

                verifyTokenUseCase(token).collect { result ->
                    when (result) {
                        is ResultHandler.Success -> {
                            onForkNavigateToApp(true)
                        }

                        is ResultHandler.Error -> {
                            when (result.error) {
                                AuthError.TokenError.EXPIRED -> {
                                    println(AuthError.TokenError.EXPIRED.name)
                                    onForkNavigateToApp(false)
                                }
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
            } catch (e: NullPointerException) {
                onForkNavigateToApp(false)
            }
        }
    }
}