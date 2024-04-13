package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventCreateUpdateDto
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

class UpdateEventUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(
        event: EventCreateUpdateDto,
        id: String
    ): Flow<ResultHandler<HttpResponse, DataError.NetworkError>> =
        networkHandler.invokeApi { updateEvent(event, id, databaseClient.selectUserToken()) }
}