package all_events_screen.domain.use_case

import all_events_screen.data.CategoriesWithCountDto
import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class LoadCategoriesWithCountUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<CategoriesWithCountDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getCategoriesWithCount(databaseClient.selectUserToken()) }
}