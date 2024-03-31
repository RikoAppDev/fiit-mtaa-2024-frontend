package core.domain

import core.data.remote.KtorClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkHandler(private val networkClient: KtorClient) {
    fun <T> invokeApi(call: suspend KtorClient.() -> T): Flow<ResultHandler<T, DataError.NetworkError>> =
        flow {
            try {
                emit(ResultHandler.Loading())
                val result = networkClient.call()
                emit(ResultHandler.Success(result))
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
            } catch (e: Exception) {
                emit(ResultHandler.Error(DataError.NetworkError.UNKNOWN))
            }
        }
}