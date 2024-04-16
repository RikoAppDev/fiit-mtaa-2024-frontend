package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventCreateUpdateDto
import event.data.dto.EventCreateUpdateRespDto
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

class EndEventUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<HttpResponse, DataError.NetworkError>> =
        networkHandler.invokeApi { endEvent(id, databaseClient.selectUserToken()) }
}