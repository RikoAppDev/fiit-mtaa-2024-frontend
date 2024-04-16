package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.PlacesResponseDto
import kotlinx.coroutines.flow.Flow

class GetPlacesUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(query: String): Flow<ResultHandler<PlacesResponseDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getPlaces(query, databaseClient.selectUserToken()) }
}