package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class SignInForEventUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<String, DataError.NetworkError>> =
        networkHandler.invokeApi { signInForEvent(id, databaseClient.selectUserToken()) }
}