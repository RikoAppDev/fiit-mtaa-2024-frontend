package account_detail.domain.use_case

import account_detail.domain.model.UpdateUser
import auth.data.remote.dto.AuthUserDto
import auth.domain.model.NewUser
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.ResultHandler
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateUserUseCase(private val networkClient: KtorClient) {
    operator fun invoke(updateUser: UpdateUser, token:String): Flow<ResultHandler<String, DataError.NetworkError>> =
        flow {
            try {
                emit(ResultHandler.Loading())
                val response = networkClient.updateUser(updateUser, token)
                emit(ResultHandler.Success(response))
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
                println(e)
                emit(ResultHandler.Error(DataError.NetworkError.UNKNOWN))
            }
        }
}