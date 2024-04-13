package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.CategoriesDto
import kotlinx.coroutines.flow.Flow

class GetAllCategoriesUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<CategoriesDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getAllCategories(databaseClient.selectUserToken()) }
}