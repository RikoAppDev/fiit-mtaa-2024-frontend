package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventDetailDto
import kotlinx.coroutines.flow.Flow

class LoadEventDataUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<EventDetailDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getEventDetail(id, databaseClient.selectUserToken()) }
}