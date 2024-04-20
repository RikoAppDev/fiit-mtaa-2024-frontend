package home_screen.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.data.remote.dto.EventCardListDto
import core.domain.DataError
import core.domain.GpsPosition
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class GetNearestEventsUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(actualLocation: GpsPosition): Flow<ResultHandler<EventCardListDto, DataError.NetworkError>> =
        networkHandler.invokeApi {
            getNearestEvents(
                actualLocation,
                databaseClient.selectUserToken()
            )
        }
}