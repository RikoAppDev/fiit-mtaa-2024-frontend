package events_on_map_screen.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventDetailDto
import events_on_map_screen.data.PointListDto
import kotlinx.coroutines.flow.Flow

class LoadPointsUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<PointListDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getPointsOnMap(databaseClient.selectUserToken()) }
}