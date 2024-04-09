package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.data.remote.dto.EventCardListDto
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class LoadMyEventsUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<EventCardListDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getMyEvents(databaseClient.selectUserToken()) }
}