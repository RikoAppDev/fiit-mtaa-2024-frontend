package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class SignOffEventUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<String, DataError.NetworkError>> =
        networkHandler.invokeApi { signOffEvent(id, databaseClient.selectUserToken()) }
}