package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

class DeleteEventUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<HttpResponse, DataError.NetworkError>> =
        networkHandler.invokeApi { deleteEvent(id, databaseClient.selectUserToken()) }
}