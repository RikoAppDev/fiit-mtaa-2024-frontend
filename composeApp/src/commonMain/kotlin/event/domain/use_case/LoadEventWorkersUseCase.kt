package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventWorkersDto
import kotlinx.coroutines.flow.Flow

class LoadEventWorkersUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<EventWorkersDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getEventWorkers(id, databaseClient.selectUserToken()) }
}