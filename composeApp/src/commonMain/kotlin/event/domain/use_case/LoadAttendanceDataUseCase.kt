package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.AttendanceDataDto
import event.data.dto.EventDetailDto
import event.data.dto.LiveEventDataDto
import kotlinx.coroutines.flow.Flow

class LoadAttendanceDataUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(id: String): Flow<ResultHandler<AttendanceDataDto, DataError.NetworkError>> =
        networkHandler.invokeApi { getAttendanceData(id, databaseClient.selectUserToken()) }
}