package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventCreateUpdateDto
import event.data.dto.EventCreateUpdateRespDto
import event.presentation.reporting.data.dto.ReportingItemsListDto
import kotlinx.coroutines.flow.Flow

class LoadReportingUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient,
) {
    operator fun invoke(id: String): Flow<ResultHandler<ReportingItemsListDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getReporting(id, databaseClient.selectUserToken()) }
}