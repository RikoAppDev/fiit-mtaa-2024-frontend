package auth.domain.use_case

import auth.data.remote.dto.RegisterUserDto
import auth.domain.model.NewUser
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUserUseCase(private val networkClient: KtorClient) {
    operator fun invoke(newUser: NewUser): Flow<ResultHandler<RegisterUserDto, DataError.NetworkError>> =
        flow {
            try {
                emit(ResultHandler.Loading())
                val registerUserDto = networkClient.registerUser(newUser = newUser)
                emit(ResultHandler.Success(registerUserDto))
            } catch (e: RedirectResponseException) {
                // 3xx - responses
                emit(ResultHandler.Error(DataError.NetworkError.REDIRECT))
            } catch (e: ClientRequestException) {
                // 4xx - responses
                emit(ResultHandler.Error(DataError.NetworkError.BAD_REQUEST))
            } catch (e: ServerResponseException) {
                // 5xx - responses
                emit(ResultHandler.Error(DataError.NetworkError.SERVER_ERROR))
            } catch (e: NoTransformationFoundException) {
                emit(ResultHandler.Error(DataError.NetworkError.SERIALIZATION))
            } catch (e: Exception) {
                emit(ResultHandler.Error(DataError.NetworkError.UNKNOWN))
            }
        }
}