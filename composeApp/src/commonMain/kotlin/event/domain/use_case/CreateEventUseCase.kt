package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventCreateUpdateDto
import event.data.dto.EventCreateUpdateRespDto
import kotlinx.coroutines.flow.Flow

class CreateEventUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(event: EventCreateUpdateDto): Flow<ResultHandler<EventCreateUpdateRespDto, DataError.NetworkError>> =
        networkHandler.invokeApi { createEvent(event, databaseClient.selectUserToken()) }
}