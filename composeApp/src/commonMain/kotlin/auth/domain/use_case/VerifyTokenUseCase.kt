package auth.domain.use_case

import auth.domain.AuthError
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import core.domain.DataError
import core.domain.Error
import core.domain.ResultHandler
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VerifyTokenUseCase(
    private val networkClient: KtorClient,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<Boolean, Error>> = flow {
        try {
            emit(ResultHandler.Loading())
            val token = databaseClient.selectUserToken()
            val valid = networkClient.verifyUserToken(token)
            if (valid) {
                emit(ResultHandler.Success(true))
            } else {
                emit(ResultHandler.Error(AuthError.TokenError.EXPIRED))
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            emit(ResultHandler.Error(DataError.NetworkError.RESPONSE))
        } catch (e: ClientRequestException) {
            // 4xx - responses
            when (e.response.status) {
                HttpStatusCode.BadRequest -> emit(ResultHandler.Error(DataError.NetworkError.BAD_REQUEST))
                HttpStatusCode.Unauthorized -> emit(ResultHandler.Error(DataError.NetworkError.UNAUTHORISED))
                HttpStatusCode.Forbidden -> emit(ResultHandler.Error(DataError.NetworkError.FORBIDDEN))
                HttpStatusCode.NotFound -> emit(ResultHandler.Error(DataError.NetworkError.NOT_FOUND))
                HttpStatusCode.RequestTimeout -> emit(ResultHandler.Error(DataError.NetworkError.REQUEST_TIMEOUT))
                HttpStatusCode.Conflict -> emit(ResultHandler.Error(DataError.NetworkError.CONFLICT))
                HttpStatusCode.PayloadTooLarge -> emit(ResultHandler.Error(DataError.NetworkError.PAYLOAD_TOO_LARGE))
                HttpStatusCode.TooManyRequests -> emit(ResultHandler.Error(DataError.NetworkError.TOO_MANY_REQUESTS))
                else -> emit(ResultHandler.Error(DataError.NetworkError.CLIENT))
            }
        } catch (e: ServerResponseException) {
            // 5xx - responses
            emit(ResultHandler.Error(DataError.NetworkError.SERVER))
        } catch (e: ContentConvertException) {
            emit(ResultHandler.Error(DataError.NetworkError.SERIALIZATION))
        } catch (e: IOException) {
            emit(ResultHandler.Error(DataError.NetworkError.NO_INTERNET))
        } catch (e: NullPointerException) {
            emit(ResultHandler.Error(DataError.LocalError.NOT_LOGGED_IN))
        } catch (e: Exception) {
            println(e)
            emit(ResultHandler.Error(DataError.NetworkError.UNKNOWN))
        }
    }
}