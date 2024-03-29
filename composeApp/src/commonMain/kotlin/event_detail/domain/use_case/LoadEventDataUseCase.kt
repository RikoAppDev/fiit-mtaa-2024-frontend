package event_detail.domain.use_case

import core.data.remote.KtorClient
import core.data.remote.dto.EventsCardListDto
import core.domain.DataError
import core.domain.ResultHandler
import event_detail.data.dto.EventDetailDto
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class LoadEventDataUseCase(private val networkClient: KtorClient, val id:String) {
    operator fun invoke(): Flow<ResultHandler<EventDetailDto, DataError.NetworkError>> =
        flow {
            try {
                emit(ResultHandler.Loading())
                val eventData = networkClient.getEventDetail(id)
                emit(ResultHandler.Success(eventData))

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
                println(e)
                emit(ResultHandler.Error(DataError.NetworkError.UNKNOWN))
            }
        }
}