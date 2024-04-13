package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.LiveEventDataDto
import kotlinx.coroutines.flow.Flow

class LoadInProgressEventDataUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<LiveEventDataDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getLiveEventData(id, databaseClient.selectUserToken()) }
}