package home_screen.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.data.remote.dto.EventCardListDto
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import home_screen.data.ActiveEventDto
import kotlinx.coroutines.flow.Flow

class GetActiveEventUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<ActiveEventDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getActiveEvent(databaseClient.selectUserToken()) }
}