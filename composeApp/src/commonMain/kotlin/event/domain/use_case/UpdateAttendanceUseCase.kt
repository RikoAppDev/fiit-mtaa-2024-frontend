package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.AttendanceDataDto
import event.data.dto.AttendanceUpdateListDto
import event.data.dto.EventCreateUpdateDto
import event.data.dto.EventCreateUpdateRespDto
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

class UpdateAttendanceUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(
        id: String,
        attendance: AttendanceUpdateListDto
    ): Flow<ResultHandler<HttpResponse, DataError.NetworkError>> =
        networkHandler.invokeApi {
            updateAttendance(
                id = id,
                token = databaseClient.selectUserToken(),
                attendance = attendance
            )
        }
}