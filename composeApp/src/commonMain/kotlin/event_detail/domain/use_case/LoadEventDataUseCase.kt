package event_detail.domain.use_case

import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event_detail.data.dto.EventDetailDto
import kotlinx.coroutines.flow.Flow

class LoadEventDataUseCase(private val networkHandler: NetworkHandler) {
    operator fun invoke(id: String): Flow<ResultHandler<EventDetailDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getEventDetail(id) }
}