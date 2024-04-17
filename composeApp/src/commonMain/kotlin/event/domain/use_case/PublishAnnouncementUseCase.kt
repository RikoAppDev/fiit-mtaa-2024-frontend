package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.EventCreateUpdateDto
import event.data.dto.EventCreateUpdateRespDto
import kotlinx.coroutines.flow.Flow

class PublishAnnouncementUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient,
) {
    operator fun invoke(
        id: String,
        message: String,
    ): Flow<ResultHandler<String, DataError.NetworkError>> =
        networkHandler.invokeApi {
            publishAnnouncementMessage(
                id,
                databaseClient.selectUserToken(),
                message
            )
        }
}