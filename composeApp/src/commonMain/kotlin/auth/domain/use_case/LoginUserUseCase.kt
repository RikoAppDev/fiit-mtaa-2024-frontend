package auth.domain.use_case

import auth.data.remote.dto.AuthUserDto
import auth.domain.model.Login
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUserUseCase(private val networkClient: KtorClient) {
    operator fun invoke(login: Login): Flow<ResultHandler<AuthUserDto, DataError.NetworkError>> =
        flow {
            try {
                emit(ResultHandler.Loading())
                val loginDto = networkClient.loginUser(login)
                emit(ResultHandler.Success(loginDto))
            } catch (e: RedirectResponseException) {
                // 3xx - responses
                emit(ResultHandler.Error(DataError.NetworkError.REDIRECT))
            } catch (e: ClientRequestException) {
                // 4xx - responses
                emit(ResultHandler.Error(DataError.NetworkError.BAD_REQUEST))
            } catch (e: ServerResponseException) {
                // 5xx - responses
                emit(ResultHandler.Error(DataError.NetworkError.SERVER_ERROR))
            } catch (e: ContentConvertException) {
                emit(ResultHandler.Error(DataError.NetworkError.SERIALIZATION))
            } catch (e: Exception) {
                emit(ResultHandler.Error(DataError.NetworkError.UNKNOWN))
            }
        }
}