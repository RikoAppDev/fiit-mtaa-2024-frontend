package event_detail.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event_detail.data.dto.EventDetailDto
import event_detail.data.dto.EventWorkersDto
import kotlinx.coroutines.flow.Flow

class LoadEventWorkers(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<EventWorkersDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getEventWorkers(id, databaseClient.selectUserToken()) }
}